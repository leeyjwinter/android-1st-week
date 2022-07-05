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




<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



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

           
<p align="center">  <img src="https://velog.velcdn.com/images/leeyjwinter/post/a6f825e8-9231-4a25-990a-409a8c3c427e/image.png"  width="800px"></p>
 
 

* 앱을 처음 실행화면 json 파일에서 불러온 전화번호부 목록이 나타납니다.

* 상단의 메뉴바를 터치하거나 좌측 끝에서 우측으로 스와이프를 하면 Drawer 바가 열리며 여기서 전화 앱, 카메라 앱, madcamp.io 사이트로 이동할 수 있습니다. 

***

#### 기술 설명 

* ActionBarDrawerToggle 버메서드를 활용해서 drawer을 열고 닫습니다.
* navigation bar의 헤더 xml 파일과 menu 폴더의 navigation xml 파일을 생성하여 drawer의 디자인을 만들고 setNavigationItemSelectedListener함수를 통해 button 에 따른 앱 실행을 제어합니다. 


***

### 2. 탭1 - 연락처


![](https://velog.velcdn.com/images/leeyjwinter/post/4aade709-3567-4815-b131-4b1bdc2a73d2/image.png)


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


<p align="center">  <img src="https://velog.velcdn.com/images/leeyjwinter/post/62ae233e-845b-4b67-8418-daf33bc0042c/image.png" width = "1000px"></p>


* 스와이프를 하여 탭2로 넘어가면 갤러리 앱에서 연동되어 사진을 볼 수 있습니다. 사진 항목을 터치하여 확대된 사진을 볼 수 있고 좌, 우 버튼을 통하여 다른 사진으로 이동할 수 있습니다. 
* drawer 바의 카메라 앱을 통하여 찍은 사진도 갤러리 탭에 저장할 수 있으며 새로고침 버튼을 통하여 추가된 사진을 확인할 수 있습니다.
* 출근부 버튼은 탭3와 연동되어 하루동안 나의 출, 퇴근, 몰입시간을 한 눈에 볼 수 있는 사진들의 목록을 보여줍니다.


***

#### 기술 설명

***

***
### 탭3 - 몰입 품은 타이머


***
#### 기술 설명


