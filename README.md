<div id="top"></div>


<p align="center">
<img src="https://velog.velcdn.com/images/leeyjwinter/post/39aec705-afdd-4b0f-b104-549e6e516e1b/image.png" height="300px" width="300px">
</p>



<h3 align="center">MAD SCHEDULER</h3>

  <p align="center">
    <br />
    <a href="https://github.com/leeyjwinter/android-1st-week">
      <strong>Explore the docs »</strong></a>
  </p>


<!-- ABOUT THE PROJECT -->
## About The Project


❗️ NOTE: To use this application, DOWNLOAD CatHub.apk file and install it. It only works on ANDROID OS.
* 임의의 연락처를 띄우는 탭1, 
* 갤러리 앱 속 사진들을 출력하는 탭2, 
* 열품타처럼 나의 몰입시간을 기록하고 한 주의 기록을 확인할 수 있는 탭3 로 구성했습니다.

* 사용자는 출근을 한다음 시간을 기록하고, 공부 시작시간, 중지, 종료 및 퇴근 버튼으로 몰입 시간을 실시간으로 볼 수 있으며, 그래프를 통하여 그 주의 몰입 시간을 확인할 수 있습니다.

* 퇴근버튼을 누르면 자동으로 화면을 캡처하여, 갤러리에서 나의 출근부 기록을 볼 수 있습니다. 

***

### 개발 팀원
* KAIST 전산학부 방준형
* 성균관대 컴퓨터교육과 이영준
***

### 개발 환경

* OS: Android (minSdk: 21, targetSdk 32)
* Language: Kotlin
* IDE: Android Studio
* Target Device: Galaxy S10e

***


## 어플리케이션 소개


### 1. 초기화면과 Drawer

           
<p align="center">  <img src="https://user-images.githubusercontent.com/86416273/177321634-d814fd58-8bce-4fa9-9895-9b2939753c27.png"  width="1000px"></p>

 

* 앱을 처음 실행화면 json 파일에서 불러온 전화번호부 목록이 나타납니다.

* 상단의 메뉴바를 터치하거나 좌측 끝에서 우측으로 스와이프를 하면 Drawer 바가 열리며 여기서 전화 앱, 카메라 앱, madcamp.io 사이트로 이동할 수 있습니다. 

***

#### 기술 설명 

* ActionBarDrawerToggle 버메서드를 활용해서 drawer을 열고 닫습니다.
* navigation bar의 헤더 xml 파일과 menu 폴더의 navigation xml 파일을 생성하여 drawer의 디자인을 만들고 setNavigationItemSelectedListener함수를 통해 button 에 따른 앱 실행을 제어합니다. 


***

### 2. 탭1 - 연락처

<p align="center">  <img src="https://user-images.githubusercontent.com/86416273/177322105-7bc225f1-fea1-4beb-b411-cdfb7151e8a0.png"  width="800px"></p>


* 성별의 유무, 성별이 있는 경우 남/여 인지에 따라 사용자에게 보여지는 아이콘을 다르게 합니다.
* 임의의 json 파일에 있는 연락처 정보들을 받아와 화면에 출력합니다.
* 상세 버튼을 누르면 각 연락처 정보를 보다 더 상세히 파악할 수 있으며, 상세 탭의 전화 버튼을 눌러 바로 전화를 걸 수 있습니다.
* 삭제 버튼을 누를 시 toast 메시지와 함께 목록에서 해당 사람의 연락처가 사라집니다. 

***

#### 기술 설명 

* phonebook.json에 이름, 번호, 성별을 담은 jsonarray 파일을 생성합니다. 
* 연락처 item을 연속적으로 화면에 출력하기 위하여 RecycleView를 활용합니다.
* 탭1에 해당하는 OneFragment.kt 파일에서 json 파일을 불러와 recyclerview의 adapter 함수에서 해당하는 값들을 받아 출력해줍니다.
* intent를 생성하여 modifyNumberActivity 액티비티 코틀린 파일로 상세버튼을 누른 항목의 정보를 전달해주어 새 탭에 상세 정보가 담긴 화면을 출력해줍니다.


*** 

### 탭2 - 갤러리


<p align="center">  <img src="https://user-images.githubusercontent.com/86416273/177321748-4d669299-2556-43d0-8e3e-30d162e2c02e.png" width = "1000px"></p>





* 스와이프를 하여 탭2로 넘어가면 갤러리 앱에서 연동되어 사진을 볼 수 있습니다. 사진 항목을 터치하여 확대된 사진을 볼 수 있고 좌, 우 버튼을 통하여 다른 사진으로 이동할 수 있습니다. 확대된 사진은 두 손가락으로 추가로 확대하거나 축소할 수 있습니다.
* drawer 바의 카메라 앱을 통하여 찍은 사진도 갤러리 탭에 저장할 수 있으며 새로고침 버튼을 통하여 추가된 사진을 확인할 수 있습니다.
* 출근부 버튼은 탭3와 연동되어 하루동안 나의 출, 퇴근, 몰입시간을 한 눈에 볼 수 있는 사진들의 목록을 보여줍니다.


***

#### 기술 설명

***
* 갤러리에서 사진을 읽고 쓰기 위해 READ & WRITE_EXTERNAL_STORAGE를 허용해 작업하였습니다.
* MEDIASTORE를 활용해 휴대폰 외부저장소의 모든 이미지를 불러오며, RELATIVE_PATH 옵션으로 특정 경로(출근부)의 사진을 따로 불러올 수 있습니다.
* 불러온 이미지를 girdview를 사용해 로딩하며, 각각의 이미지를 클릭하면 subactivity를 호출해 이미지를 크게 볼 수 있습니다. 스크롤 시 decodepath가 반복됨에 의해 속도가 느려지는 것을 방지하기 위해 LruCache를 사용하여 이미 불러온 사진을 다시 불러오지 않도록 캐싱하였습니다.
* 사진 항목을 크게 보여주는 subactivity에는 PhotoView를 적용해 두 손가락으로 확대 축소가 가능하게 하였습니다. 또, activity간의 call과 return에 관련된 함수를 활용해 좌우 버튼으로 이전/다음 사진을 불러오는 activity를 호출할 수 있도록 작업하였습니다.
* 새로고침 기능으로 이미지를 다시 불러와 카메라 기능 or 몰입 타이머 스크린 캡처 기능으로 갤러리에 추가된 이미지도 바로 girdview상으로 불러올 수 있도록 하였습니다.

***
### 탭3 - 몰입 품은 타이머

![](https://velog.velcdn.com/images/junhyeong0411/post/9ae1b8dc-6fa5-4d32-b1b8-ca07968ae0ee/image.png)


* 스와이프를 하여 탭 3으로 넘어가면 "몰입 품은 타이머"가 나옵니다. 출근한 시간을 지정한 후 "출근" 버튼을 눌러서 오늘의 출근 시간을 저장해놓을 수 있습니다. (출근 시간은 잘못 입력했을 경우 이후에 다시 스와이핑하여 수정이 가능합니다)  
* 시작 버튼을 누르면 오늘의 몰입 타이머가 작동하기 시작합니다. 이때 기록된 타이머는 앱이 중간에 종료되더라도 유지되며, 중지 버튼을 통해 몰입 시간 기록을 중지하고 휴식할 수 있습니다.

![](https://velog.velcdn.com/images/junhyeong0411/post/cdbbc063-5e3b-4232-af80-efd80fbd7a84/image.png)


* "퇴근 및 제출" 버튼을 눌러야 그날의 몰입이 종료됩니다. 혹시 몰입 중에 밤 12시가 지나더라도, "퇴근 및 제출" 버튼을 누르지 않으면 계속해서 이전 날짜로 기록할 수 있습니다. 퇴근한 후 새로운 날짜에 다시 "시작" 버튼을 누르면 다음 날의 몰입이 기록되기 시작합니다.
* "퇴근 및 제출" 버튼을 누르면 그날의 몰입 기록이 자동으로 스크린샷으로 저장됩니다. 그날의 출근 시간과 몰입한 시간이 함께 기록되어 있으며, 2번 탭의 갤러리에서 "출근부" 버튼을 통해 몰입 기록을 모은 사진들을 따로 확인할 수 있습니다. 사진이 추가된 것을 확인하려면 새로고침 버튼을 누르거나 탭을 위로 한 번 스와이핑하여 새로고침해야 합니다.
* 오늘부터 총 7일 동안의 기록이 위의 그래프에 누적되어 저장됩니다. 이번 주에 내가 얼마나 꾸준히 몰입하였는지 한 눈에 알아볼 수 있습니다. 그래프는 확대 축소 등이 가능합니다.
* 가운데에 있는 타이머를 터치하여 타이머 테마를 바꿀 수 있습니다. 테마는 기본, 장작불, 숲, 빗소리로 총 4가지가 있습니다. 추가로, 소리 버튼을 터치해 켜면 테마에 맞는 ASMR이 나와 몰입에 도움을 주도록 구성되었습니다. 소리 버튼을 한번 더 터치해 소리를 끌 수 있습니다.
***
#### 기술 설명

***
* 타이머를 구현하기 위해 chronometer를 사용하였습니다.
* 일주일간의 몰입 시간과 출근 시간 등을 기록하기 위해 Sharedpreferences를 사용해 데이터를 저장하였습니다.
* 스크린샷 캡처 및 저장을 위해 외부 저장소에 접근하였습니다. rootView를 기준으로 앱 화면 전체를 캡처하여 특정 디렉토리에 저장하였으며, 이 디렉토리는 2번 탭에서 특정 경로만 표시하는 데에 활용하였습니다.
* 테마를 gif 형태로 집어넣기 위해 Glide를 사용하였으며, 음악 재생을 위해 MediaPlayer를 사용하였습니다. 

***


