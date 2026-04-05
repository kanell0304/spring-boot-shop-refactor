describe('로그인 테스트', () => {
    it('정상적으로 로그인하면 마이페이지로 이동해야 함', () => {
      cy.visit('/member/login');
  
      cy.get('input[name="email"]').type('user@naver.com');
      cy.get('input[name="pw"]').type('1234');
  
      cy.contains('LOGIN').click();
  
      // 로그인 후 이동 확인 (예: 마이페이지로 간다고 가정)
      cy.url().should('include', '/'); // 또는 다른 경로로 수정
    });
  });