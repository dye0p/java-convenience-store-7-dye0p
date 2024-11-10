# java-convenience-store-precourse

---

## 프로그램 소개

구매한 상품에 대하여 할인 혜택과 재고 상황을 고려하여 최종 결제 금액을 계산하고 영수증 형태로 안내하는 간편 편의점 결제 시스템 입니다.

## 프로그램 사용 가이드

아래는 프로그램 사용 예시 입니다. (입력값은 예시 입니다.) 기능에 대한 세부 사항은 구현 기능 목록을 통해서 확인하실 수 있습니다.

0. 프로그램을 실행하면 안내와 함께 상품의 목록이 출력 됩니다.

```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개
```

1. 구매할 상품과 수량을 입력 합니다. (상품명, 수량은 하이폰(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분해 입력합니다.)

```
구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]
```

1-1. 프로모션 적용이 가능한 상품에 대해 수량만큼 가져오지 않았다면, 혜택에 대한 안내 메세지와 함께 상품 추가 여부를 입력할 수 있습니다.

```
현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y
```

1-2. 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 안내 메세지와 함께 일부 수량에 대해 정가로 결제할지 여부를 입력할 수 있습니다.

```
현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y
```

2. 멤버십 할인 적용 여부 안내 메세지와 함께 멤버십 할인 결정 여부를 입력할 수 있습니다.

```
멤버십 할인을 받으시겠습니까? (Y/N)
Y
```

3. 모든 헤택과 멤버십 할인 여부를 결정했다면 구매 상품 내역, 증정 상품 내역, 금액 정보를 영수증 형태로 확인할 수 있습니다.

```
===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000
```

4. 추가 구매 여부 안내 메세지와 함께 추가 구매 여부를 입력할 수 있습니다.

```
감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y
```

### 실행 결과 예시

```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-3],[에너지바-5]

멤버십 할인을 받으시겠습니까? (Y/N)
Y 

===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

===========W 편의점=============
상품명		수량	금액
콜라		10 	10,000
===========증	정=============
콜라		2
==============================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
Y

안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 재고 없음 탄산2+1
- 콜라 1,000원 7개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[오렌지주스-1]

현재 오렌지주스은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
Y

===========W 편의점=============
상품명		수량	금액
오렌지주스		2 	3,600
===========증	정=============
오렌지주스		1
==============================
총구매액		2	3,600
행사할인			-1,800
멤버십할인			-0
내실돈			 1,800

감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
N
```

## 기능 구현 목록

### 입력 기능

- [x] 상품 목록과 행사 목록을 파일 입출력을 통해 불러오는 기능
    - src/main/resources/products.md과 src/main/resources/promotions.md 파일을 이용한다.
    - 두 파일 모두 내용의 형식을 유지한다면 값은 수정할 수 있다.
- [x] 구매할 상품과 수량을 입력 받는 기능
    - 상품명, 수량은 하이폰(-)으로 구분한다.
    - 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
        ```
        [콜라-10],[사이다-3]
        ```

- [x] 프로모션 적용이 가능한 상품의 수량 추가 여부를 입력받는 기능
- [x] 프로모션 재고가 부족한 일부 수량을 정가로 결제할지 여부를 입력받는 기능
- [ ] 멤버십 할인 적용 여부를 입력받는 기능
- [ ] 추가 구매 여부를 입력받는 기능

### 핵심 기능

- [x] 구매 수량과 재고 수량을 비교해 결제 가능 여부를 확인하는 기능
- [ ] 결제된 수량만큼 해당 상품의 재고에서 차감하는 기능
- [x] 고객이 입력한 상품이 프로모션 상품인지 확인하는 기능
    - `1+1` 또는 `2+1` 프로모션이 각각 지정된 상품에만 프로모션 할인 적용 가능
        - `1+1` -> MD추천상품, 반짝할인 (프로모션)
        - `2+1` -> 탄산2+1 (프로모션)
- [x] 오늘 날짜를 확인해서 프로모션 기간인지 확인하여 프로모션 혜택을 받을수 있는지 확인하는 기능
    - 프로모션 상품이라고 하더라도 오늘이 프로모션 기간 내에 포함되지 않으면 프로모션 할인은 받을 수 없다.
- [x] 프로모션 상품이 아니라면 일반 결제를 진행하는 기능
- [x] 프로모션 적용이 가능한 상품에 대해 고객이 프로모션 혜택만큼 수량을 가져왔는지 확인하는 기능
    - [ ] 프로모션 혜택 만큼 수량을 가져왔다면 안내 메세지를 출력하지 않고 할인을 한다.
    - [x] 프로모션 혜택 만큼 수량을 가져오지 않았다면 사용자의 입력에 따라 혜택을 추가 하는 기능
        - [x] 안내 메세지의 결과가 `Y`라면 할인을 추가한다.
        - [x] 안내 메세지의 결과가 `N`라면 할인을 추가하지 않는다.
- [x] 프로모션 기간 중이라면 프로모션 재고를 우선 차감하는 기능
- [x] 프로모션 재고가 부족한 경우에는 일반 재고를 사용하는 기능
- [x] 프로모션 재고가 고객이 고른 상품의 개수보다 부족한 경우 사용자의 입력에 따라 일부 수량에 대해 정가로 결제하는 기능
- [ ] 멤버십 할인을 하는 기능
- [ ] 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산하는 기능
    - [ ] 사용자가 입력한 상품의 가격과 수량을 기반으로 최종 결제 금액을 계산하는 기능
    - [ ] 상품별 가격과 수량을 곱하여 총구매액을 계산하는 기능
    - [ ] 프로모션 및 멤버십 할인 정책을 반영하여 최종 결제 금액을 산출하는 기능
- [ ] 사용자가 잘못된 값을 입력할 경우 `IllegalArgumentException`를 발생시키고, "[ERROR]"로 시작하는 에러 메시지를 출력 후 그 부분부터 입력을 다시 받는 기능
    - `Exception`이 아닌 `IllegalArgumentException`, `IllegalStateException` 등과 같은 명확한 유형을 처리한다.

### 출력 기능

- [x] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 출력하는 기능
    - 만약 재고가 0개라면 `재고 없음`을 출력한다.

- 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우
- [x] 혜택에 대한 안내 메세지를 출력하는 기능


- 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우,
- [x] 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메세지 출력 기능


- [ ] 멤버십 할인 적용 여부 안내 메세지 출력 기능
- [ ] 구매 상품 내역, 증정 상품 내역, 금액 정보를 영수증의 형태로 출력 하는 기능
    - 구성 요소를 보기 좋게 정렬하여 고객이 쉽게 확인할 수 있도록 출력한다.
- [ ] 추가 구매 여부 안내 메세지 출력 기능
- [ ] "[ERROR]"로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력 하는 기능
    - 구매할 상품과 수량 형식이 올바르지 않은 경우
        - `[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
    - 존재하지 않는 상품을 입력한 경우
        - `[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
    - 구매 수량이 재고 수량을 초과한 경우
        - `[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
    - 기타 잘못된 입력의 경우
        - `[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

### 예외 상황

잘못된 입력으로 인해 예외가 발생할 경우 IllegalArgumentException 을 발생시키고, 예외 메세지를 출력한 후, 그 부분부터 다시 입력 받을 수 있다.

잘못된 상품과 수량 입력에 대한 예외 상황

- 구매할 상품과 수량 형식이 올바르지 않은 경우
    - [x] 상품 이름과 수량을 하이폰(-)으로 구별하지 않은 경우
    - [x] 개별 상품을 대괄호([])로 묶지 않은 경우
    - [ ] 각각의 상품을 쉼표(,)로 구분하지 않은 경우
- [x] 존재하지 않는 상품을 입력한 경우
- [x] 구매 수량이 재고 수량을 초과한 경우
- 기타 잘못된 입력의 경우
    - [x] null 또는 빈 값을 입력한 경우
    - [x] 공백을 포함해서 입력한 경우

### 프로그램 흐름도

1. 구매할 상품과 수량을 입력 받는다.
2. 구매 수량과 재고 수량을 비교해 결제 가능 여부를 확인한다.
2. 프로모션 할인이 가능한지 확인한다.
3. 프로모션 할인이 가능하다면 할인을 진행한다.
    - 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
    - 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
    - 입력에 따라서 할인 여부를 판단한다.
2. 멤버십 할인 적용 여부를 입력 받는다.
3. 입력에 따라서 멤버십 할인을 한다.
3. 사용자가 입력한 상품의 사격과 수량을 기반으로 최종 결제 금액을 계산한다.
4. 구매 수량에 따라서 재고를 차감한다.
4. 구매 내역과 산출한 금액 정보를 영수증으로 출력한다.
5. 추가 구매를 진행할지 종료할지 입력 받는다.
