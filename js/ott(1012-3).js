
// 글로벌 변수 선언
let X_train, X_test, y_train, y_test;
let model;
let labelNames = ['자가', '전세', '월세'];
let XnormParams; // 전역 변수로 XnormParams 선언
let loadedModel; // 전역 변수로 loadedModel 선언

// Min-Max 정규화 함수
function MinMaxScaling(tensor, prevMin = null, prevMax = null) {
    const min = prevMin || tensor.min();
    const max = prevMax || tensor.max();
    const normedTensor = tensor.sub(min).div(max.sub(min));
    return {
        tensor: normedTensor,
        min,
        max
    };
}

// 페이지 로드 시 실행되는 함수
window.onload = async function() {
    await run();
    plot();
    // 모델 로딩 (한 번만 로드)
    loadedModel = await tf.loadLayersModel('indexeddb://my-model-1012(2)').catch(async (error) => {
        console.log('모델을 찾을 수 없습니다. 새로 학습을 시작합니다.');
        
    });
};

async function run() {
    const broadcasting2 = tf.data.csv("/js/data1(20240927).csv", {
        columnConfigs: {
            자산: { isLabel: false },
            부채: { isLabel: false },
            경상소득: { isLabel: false },
            입주형태통합코드: { isLabel: true },
        },
        configuredColumnsOnly: true
    });

    let dataset2 = await broadcasting2.map(({ xs, ys }) => ({
        x: Object.values(xs),
        y: Object.values(ys)
    })).toArray();

    tf.util.shuffle(dataset2); // 데이터를 섞기
    run1(dataset2);
}

async function plot() {
    const data1 = [
        { index: '자산', value: 66627 },
        { index: '부채', value: 7930 },
        { index: '소득', value: 6548 }
    ];
    const data2 = [
        { index: '자산', value: 46878 },
        { index: '부채', value: 9016 },
        { index: '소득', value: 6377 }
    ];
    const data3 = [
        { index: '자산', value: 13479 },
        { index: '부채', value: 3403 },
        { index: '소득', value: 4201 }
    ];
    // 차트 생성 함수
    function createChart(canvasId, data) {
        const ctx = document.getElementById(canvasId).getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map(d => d.index),
                datasets: [{
                    label: '값(단위:만원)',
                    data: data.map(d => d.value),
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    x: {
                        ticks: {
                            maxRotation: 0,
                            minRotation: 0
                        }
                    }
                }
            }
        });
    }

    // 각 캔버스에 차트 그리기
    createChart('chart1', data1);
    createChart('chart2', data2);
    createChart('chart3', data3);
}

async function run1(dataset2) {
    let featureValues = dataset2.map(v => v.x);
    let labelValues = dataset2.map(v => v.y - 1); // 입주형태통합코드가 1부터 시작하므로 -1

    const featureTensor = tf.tensor2d(featureValues, [featureValues.length, 3]);

    const flatLabelValues = labelValues.flat();
    let labelTensor = tf.tensor1d(flatLabelValues, "int32");
    labelTensor = tf.oneHot(labelTensor, 3);

    // train/test split 75/25
    const trainLen = Math.floor(featureTensor.shape[0] * 0.75);
    const testLen = featureTensor.shape[0] - trainLen;

    [X_train, X_test] = tf.split(featureTensor, [trainLen, testLen]);
    [y_train, y_test] = tf.split(labelTensor, [trainLen, testLen]);

    // 정규화, MinMaxScaling
    const normedXtrainTensor = MinMaxScaling(X_train);
    XnormParams = { min: normedXtrainTensor.min, max: normedXtrainTensor.max };
    X_train.dispose();
    X_train = normedXtrainTensor.tensor;
    const normedXtestTensor = MinMaxScaling(X_test, XnormParams.min, XnormParams.max);
    X_test.dispose();
    X_test = normedXtestTensor.tensor;

    document.getElementById("train-button").removeAttribute("disabled");
}

async function train() {
    document.getElementById("model-status").innerText = "훈련중...";

    // 모델 구성
    model = tf.sequential();

    // 입력 레이어
    model.add(tf.layers.dense({
        units: 512,
        inputShape: [3],
        activation: "relu",
        kernelInitializer: 'heNormal',
        biasInitializer: 'zeros'
    }));

    model.add(tf.layers.dropout({ rate: 0.5 }));

    // 중간 레이어 1
    model.add(tf.layers.dense({
        units: 256,
        activation: "relu",
        kernelInitializer: 'glorotNormal',
        biasInitializer: 'zeros'
    }));

    model.add(tf.layers.dropout({ rate: 0.4 }));

    // 중간 레이어 2
    model.add(tf.layers.dense({
        units: 128,
        activation: "relu",
        kernelInitializer: 'heNormal',
        biasInitializer: 'zeros'
    }));

    model.add(tf.layers.dropout({ rate: 0.3 }));

    // 출력 레이어
    model.add(tf.layers.dense({
        units: 3,
        activation: "softmax",
        kernelInitializer: 'glorotUniform',
        biasInitializer: 'zeros'
    }));

    model.compile({
        optimizer: tf.train.adam(0.0001),
        loss: 'categoricalCrossentropy',
        metrics: ['accuracy']
    });

    const earlyStopping = tf.callbacks.earlyStopping({
        monitor: 'val_loss',
        patience: 10,
    });

    // summary 시각화
    const surface3 = document.querySelector("#summary");
    tfvis.show.modelSummary(surface3, model);

    const surface4 = document.querySelector('#fitCallback');

    const classWeights = {
        0: 1.4,
        1: 1.8,
        2: 1
    };

    const history = await model.fit(X_train, y_train, {
        epochs: 30,
        batchSize: 32,
        validationData: [X_test, y_test],
        classWeight: classWeights,
        callbacks: tfvis.show.fitCallbacks(surface4, [
            "loss",
            "acc",
            "val_loss",
            "val_acc"
        ])
    });

    // 모델 저장
    await model.save('indexeddb://my-model-1012(2)');
    loadedModel = model; // 모델을 전역 변수에 할당

    document.getElementById("predict-button").removeAttribute("disabled");
    document.getElementById("model-status").innerHTML = "학습종료, 예측시작";

    // 혼동 행렬 및 정확도 시각화는 생략하겠습니다.
    //confusion matrix 시각화
    const pred = model.predict(X_test);
    pred.print() //예측
    y_test.print() //정답

    //비교
    pred.argMax(1).print()
    y_test.argMax(1).print()
    //행(axis =0) ↓, 또는 열(axis =1) →을 따라 가장 큰 값의 색인을 찾는다. 기본적으로 가장 큰 값의 인덱스는 배열을 평면화하여 찾는다.
    //반환: 전체 배열에서 가장 높은 값을 가진 요소의 인덱스 반환

   const trueValue =tf.tidy(()=>y_test.argMax(1));
   const predValue =tf.tidy(()=> pred.argMax(1));

   const confusionMatrix = await tfvis.metrics.confusionMatrix(
       trueValue,
       predValue
   );

   document.getElementById('matrixTitle').innerHTML = "<h3>confusion matrix 시각화</h3><p>테스트 데이터를 이용하여 학습한후 예측한 정답률을 matrix로 표현함</p>"
   let container = document.querySelector("#matrix");
   tfvis.render.confusionMatrix(container,{values:confusionMatrix});

   //per class accuracy 시각화
   //클라스 별로 얼마나 정확하게 맞추는지
   const classAccuracy = await tfvis.metrics.perClassAccuracy(
       trueValue,
       predValue
   )
   console.log(classAccuracy)
   let container1= document.querySelector("#classAccuracy");
   tfvis.show.perClassAccuracy(container1,classAccuracy)

   //memory 정리
   pred.dispose();
   trueValue.dispose();
   predValue.dispose();

}

async function predict() {
    // 예측에 필요한 입력 값 받아오기
    const inputOne = parseFloat(document.getElementById("predict-input-1").value);
    const inputTwo = parseFloat(document.getElementById("predict-input-2").value);
    const inputThree = parseFloat(document.getElementById("predict-input-3").value);

    if (isNaN(inputOne) || isNaN(inputTwo) || isNaN(inputThree)) {
        alert("숫자만 입력 가능합니다.");
    } else {
        const features = [inputOne, inputTwo, inputThree];
        const featureTensor = tf.tensor2d([features], [1, 3]);

        // 입력 데이터를 학습 시 사용한 min, max 값으로 정규화
        const normedInputTensor = featureTensor.sub(XnormParams.min).div(XnormParams.max.sub(XnormParams.min));

        // 모델 예측
        const prediction = loadedModel.predict(normedInputTensor);
        prediction.print();  // 예측 확률값 출력

        const idx = prediction.argMax(1).dataSync()[0];
        document.getElementById(
            "predict-output"
        ).innerHTML = `예상되는 주거형태는 ${labelNames[idx]}입니다`;
    }
}
// 주요 변경 사항 설명
// 모델 로딩 위치 변경

