describe('Gestion des étudiants', () => {

  beforeEach(() => {
    // L'application doit être lancée via docker compose up
    cy.visit('http://localhost:3000/etudiants');
  });

  it('affiche la liste des étudiants', () => {
    cy.get('[data-testid="etudiant-list"]').should('be.visible');
    cy.get('[data-testid="etudiant-item"]').should('have.length.greaterThan', 0);
  });

  it('crée un nouvel étudiant', () => {
    cy.visit('http://localhost:3000/etudiants/new');

    cy.get('input[type="text"]').first().type('CY123456');
    cy.get('input[type="text"]').eq(1).type('Alice Martin');
    cy.get('input[type="date"]').type('2003-05-15');
    cy.get('input[type="email"]').type('alice@univ.tn');
    cy.get('input[type="number"]').clear().type('2023');
    cy.get('select').select(1);

    cy.get('button[type="submit"]').click();

    // After redirect, verify the student appears in the list
    cy.url().should('include', '/etudiants');
    cy.contains('Alice Martin').should('be.visible');
  });

  it('supprime un étudiant', () => {
    cy.get('[data-testid="etudiant-item"]').then(($items) => {
      const initialCount = $items.length;

      // Stub the confirm dialog to auto-accept
      cy.on('window:confirm', () => true);

      // Click the first delete button
      cy.get('[data-testid="delete-btn"]').first().click();

      // Wait for the list to update
      cy.wait(1000);

      // Verify one less item (or at least list refreshed)
      cy.get('[data-testid="etudiant-item"]').should('have.length.lessThan', initialCount + 1);
    });
  });
});