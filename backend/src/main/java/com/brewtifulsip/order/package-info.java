/**
 * 주문(order) 도메인. 주문/주문항목 생성·조회, 상태 전이(RECEIVED→PREPARING→READY).
 * 손님은 orderToken으로 조회하고, 운영자는 마스터 코드(ADR-0003)로 상태를 변경한다.
 */
package com.brewtifulsip.order;
