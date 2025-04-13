Feature: Portfolio Update

  Scenario: Updating a portfolio should calculate total value and publish event
    Given a portfolio with ticker "AAPL" and qty 10
    And the price for "AAPL" is 150
    When the portfolio is updated
    Then the total value should be 1500
    And an event should be published

  Scenario: Updating a portfolio with options should calculate total value and publish event
    Given a portfolio with ticker "AAPL_202501_C150" and qty 5
    And the price for "AAPL_202501_C150" is 20
    When the portfolio is updated
    Then the total value should be 100
    And an event should be published