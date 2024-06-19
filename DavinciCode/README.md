- Poject title = Davinci code
- Project members = 22000691 정인경

- Project description

다빈치 코드 보드게임을 자바swing을 사용하여 GUI로 구현하였다.
다빈치 코드의 규칙은 다음과 같다.

1. 흰 블록 12개, 검은 블록 12개(0~11)
2. 왼쪽부터 오른쪽 순으로 오름차순 정렬
3. 흰 블록과 검은 블록이 같은 숫자일 경우, 흰색이 왼쪽에 위치

   (2,3)번은 sort함수를 만들어서 랜덤으로 카드가 뽑혔을때 자동으로 sort되게 만들었다.
4. 첫 턴에 4개의 블록을 랜덤으로 선택한다.

   distributeTiles함수로 랜덤으로 가지게 된다.
5. 본인 턴이 되면, 뽑히지 않은 남은 블록들 중 한 개를 선택하여 본인 게임판에 넣어 다시 정렬한다.
  
   pickOne()함수로 하나를 가져오게 된다
6. 플레이어는 상대방의 블록 하나에 해당하는 숫자를 예측한다.

   GUI로 구현된 창에 입력을 하면 된다.

   <img src= 'https://github.com/22000691/Java_Final/blob/main/DavinciCode/screenshot/userInput.png?raw=true'>
7. 맞을 경우 상대방의 블록이 공개된다.

   openUserTile/computerTile중 하나로 구현했다.
8. 이 경우에는 원한다면 계속 상대방의 패를 맞출 수 있다.

   <img src= 'https://github.com/22000691/Java_Final/blob/main/DavinciCode/screenshot/Correct%20answer.png?raw=true'>
9. 계속 맞히던 중, 아무 때나 차례를 중단하고 상대방에게 넘길 수 있다.(승자의 권리!)
10. 틀릴 경우 플레이어는 그 차례에 가져갔던 블록을 공개하고 턴이 상대방에게 넘어간다.

   <img src= 'https://github.com/22000691/Java_Final/blob/main/DavinciCode/screenshot/WrongAnswer.png?raw=true'>
11. 블록이 모두 공개된 사람은 패한다. 마지막까지 패가 남아 있는 사람이 승리한다.
