// 변수 및 클래스 이름 정의
let model;
let dataPoints;
let X_train, X_test, y_train, y_test;
const numClasses = 9;
const classNames = [
    "드라마",
    "예능/오락(개인방송 콘텐츠 포함)",
    "영화",
    "교양/다큐멘터리",
    "교육/학습",
    "애니메이션(만화)",
    "스포츠",
    "뉴스",
    "기타"
];
// 클릭 시 텍스트 표시
function toggleText(container) {
    const textContent = container.querySelector('.text-content');
    textContent.style.opacity = textContent.style.opacity === '1' ? '0' : '1'; // 클릭 시 보이기/숨기기
}
// 문서가 로드된 후 실행
document.addEventListener("DOMContentLoaded", run);

// 데이터셋 로드 및 준비 함수
async function run() {
    const dataset = tf.data.csv("/js/p21v29_KMP_csv.csv", {
        columnConfigs: {
            p21gender: { isLabel: false },
            p21age: { isLabel: false },
            p21d26045: { isLabel: true }
        },
        configuredColumnsOnly: true
    });

    const filteredDataset = dataset.filter(({ xs }) => {
        return !isNaN(xs.p21gender) && xs.p21gender !== null;
    }).map(({ xs, ys }) => ({
        x: Object.values(xs),
        y: Object.values(ys)
    }));

    dataPoints = await filteredDataset.toArray();
    tf.util.shuffle(dataPoints);

    const featureValues = dataPoints.map(p => p.x);
    const labelValues = dataPoints.map(p => p.y -1);

    const featureTensor = tf.tensor2d(featureValues, [featureValues.length, 2]);
    let labelTensor = tf.tensor1d(labelValues, "int32");
    labelTensor = tf.oneHot(labelTensor, numClasses);

    const trainLen = Math.floor(featureTensor.shape[0] * 0.75);
    const testLen = featureTensor.shape[0] - trainLen;

    [X_train, X_test] = tf.split(featureTensor, [trainLen, testLen]);
    [y_train, y_test] = tf.split(labelTensor, [trainLen, testLen]);

    document.getElementById("train-button").removeAttribute("disabled");
}

// 스크롤 이벤트 핸들러
function handleScroll() {
    const slideRightElements = document.querySelectorAll(".slide_right");
    const slideLeftElements = document.querySelectorAll(".slide_left");

    slideRightElements.forEach((element) => {
        const elementPosition = element.getBoundingClientRect().top;
        const windowHeight = window.innerHeight;

        if (windowHeight > elementPosition) {
            element.classList.add("slideActive");
        } 
    });

    slideLeftElements.forEach((element) => {
        const elementPosition = element.getBoundingClientRect().top;
        const windowHeight = window.innerHeight;

        if (windowHeight > elementPosition) {
            element.classList.add("slideActive");
        } 
    });
}

// 초기 스크롤 처리
handleScroll();

// 실행
document.addEventListener("scroll", handleScroll);

// 모델 훈련 함수
async function train() {
    document.getElementById("model-status").innerHTML = `<div class="spinner-border text-center mx-5"  role="status">
    <span class="visually-hidden">Loading...</span> `// 로딩 애니메이션 추가
    document.getElementById("train-button").innerText = `준비중 입니다.`

    model = tf.sequential();
    model.add(tf.layers.dense({ inputShape: [2], units: 100, activation: "relu" }));
    model.add(tf.layers.dense({ units: 50, activation: "relu" }));
    model.add(tf.layers.dense({ units: numClasses, activation: "softmax" }));

    model.compile({
        loss: "categoricalCrossentropy",
        optimizer: "adam",
        metrics: ["accuracy"]
    });

    await model.fit(X_train, y_train, {
        epochs: 10,
        batchSize: 16,
        validationData: [X_test, y_test],
        verbose: 0
    });

    document.getElementById('train-button').innerText = "예측 완료!! ";
    document.getElementById('model-status').innerText = "준비 완료!! ";
    document.getElementById("predict-button").removeAttribute("disabled");
}

// 예측 함수
async function predict() {
    const predInputOne = parseInt(document.getElementById('predict-input-1').value);
    const predInputTwo = parseInt(document.getElementById('predict-input-2').value);

    if (isNaN(predInputOne) || isNaN(predInputTwo)) {
        alert("성별과 연령대를 선택해주세요");
    } else {
        const features = [predInputOne, predInputTwo];
        const featureTensor = tf.tensor2d(features, [1, 2]);

        const prediction = model.predict(featureTensor);
        const top3Indices = tf.tidy(() => {
            const topk = tf.topk(prediction, 3);
            return topk.indices.arraySync()[0];
        });

        featureTensor.dispose();
        prediction.dispose();

        let output = `
    <div class="row" id="predict-card" style="display:none;">
        <div class="col-10 col-md-12 mx-auto"> <!-- 모바일에서 너비 조정 -->
            <div class="card bg-dark text-white mt-5">
                <div class="card-body">
                    <h5 class="card-title">예측된 장르</h5>
                    <ul class="list-group list-group-flush">
`;

        top3Indices.forEach((index, idx) => {
            output += `
        <li class="list-group-item bg-dark text-white">
            ${idx + 1}위: ${classNames[index]}
        </li>
    `;
        });

        output += `
                    </ul>
                    <div class="text-center">
                        <button class="btn btn-primary mt-3" onclick="showRecommendations()">추천 장르 작품 보기</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
`;


        document.getElementById('predict-output').innerHTML = output;

        // 예측 카드 슬라이드 효과
        const predictCard = document.getElementById('predict-card');
        predictCard.style.display = 'block';
        predictCard.classList.add('slide-in');

        // 추천 장르 카드와 애니메이션을 함께 보여줍니다
        showRecommendations();
    }
}

// 추천 장르 카드 표시 함수
function showRecommendations() {
    const recommendCard = document.querySelector('.recommendation-card');

    // 추천 장르 카드가 처음에는 보이지 않도록 설정
    if (recommendCard.style.display === "none" || recommendCard.style.display === "") {
        recommendCard.style.display = 'block';
        recommendCard.classList.add('slide-in');
    } else {
        // 이미 보이는 경우, 클래스 제거 후 다시 슬라이드 효과
        recommendCard.classList.remove('slide-in');
        void recommendCard.offsetWidth; // 재시작을 위한 트리거
        recommendCard.classList.add('slide-in');
    }
}

// CSS에서 slide-in 클래스 정의
const style = document.createElement('style');
style.innerHTML = `
    .slide-in {
        animation: slideIn 1.0s forwards;
    }

    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
`;
document.head.appendChild(style);


// // 시각화
// window.onload = async function plot() {
//     const dataPoints = await plotdata();

//     const countMap = dataPoints.reduce((acc, val) => {
//         const index = val - 1;
//         if (index >= 0 && index < plotclassNames.length) {
//             acc[index] = (acc[index] || 0) + 1;
//         }
//         return acc;
//     }, Array(plotclassNames.length).fill(0));

//     const aggregatedData = plotclassNames.map((name, index) => ({
//         name,
//         count: countMap[index]
//     }));

//     aggregatedData.sort((a, b) => b.count - a.count);

//     const xLabels = aggregatedData.map(item => item.name);
//     const yValues = aggregatedData.map(item => item.count);

//     const barData = {
//         labels: xLabels,
//         datasets: [{
//             label: '클래스 발생 횟수',
//             data: yValues,
//             backgroundColor: 'rgba(75, 192, 192, 0.2)',
//             borderColor: 'rgba(75, 192, 192, 1)',
//             borderWidth: 1
//         }]
//     };

//     const barOptions = {
//         scales: {
//             x: {
//                 beginAtZero: true,
//                 title: {
//                     display: true,
//                     text: 'OTT 종류'
//                 }
//             },
//             y: {
//                 beginAtZero: true,
//                 title: {
//                     display: true,
//                     text: '발생 횟수'
//                 }
//             }
//         },
//         indexAxis: 'x'
//     };

//     const ctx = document.getElementById('bar-chart').getContext('2d');
//     new Chart(ctx, {
//         type: 'bar',
//         data: barData,
//         options: barOptions
//     });
// }

// // 데이터 시각화를 위한 데이터셋 로드 함수
// async function plotdata() {
//     const dataset = tf.data.csv("./p23v30_KMP_csv.csv", {
//         columnConfigs: {
//             p23d26015: { isLabel: true }
//         },
//         configuredColumnsOnly: true
//     });

//     const filteredDataset = dataset.filter(({ ys }) => !isNaN(ys.p23d26015) && ys.p23d26015 !== null);
//     const dataPoints = await filteredDataset.map(({ ys }) => ys.p23d26015).toArray();

//     return dataPoints;
// }

// // 시각화를 위한 클래스 이름 정의
// const plotclassNames = [
//     "티빙(Tving)",
//     "웨이브(Wavve)",
//     "시즌(KT Seezn)(구 olleh tv 모바일)",
//     "U+ 모바일",
//     "에브리온 TV",
//     "네이버 시리즈온",
//     "곰TV",
//     "OCN",
//     "리더스코드",
//     "기타"
// ];
