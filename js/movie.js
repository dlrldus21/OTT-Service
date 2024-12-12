// YouTube API를 로드
var tag = document.createElement('script');
tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

var player;

// YouTube IFrame API가 로드된 후 호출되는 함수
function onYouTubeIframeAPIReady() {
    player = new YT.Player('youtubeVideo', {
        events: {
            'onReady': onPlayerReady
        }
    });
}

// 플레이어가 준비되었을 때 호출되는 함수
function onPlayerReady(event) {
    var myModalEl = document.getElementById('mudo');
    myModalEl.addEventListener('show.bs.modal', function (event) {
        player.playVideo(); // 동영상 자동 재생

        // 15초 후에 동영상을 정지하고 모달을 닫기 위한 타이머 설정
        setTimeout(function () {
            player.pauseVideo(); // 동영상 정지
            var modal = bootstrap.Modal.getInstance(myModalEl);
            modal.hide(); // 모달 닫기
        }, 15000); // 15000ms = 15초
    });
}