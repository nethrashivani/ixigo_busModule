Feature: Bus Booking functionality in ixigo

Background:
  Given User launches the ixigo website


  Scenario Outline: User searches for buses between cities
    Given User launches the ixigo website
    And User navigates to bus search
    When User enters "<source>" and "<destination>"
    And User selects travel date and clicks search
    Then Bus results should be displayed
    Examples:
      | source  | destination |
      | Chennai | Salem       |  
        

  Scenario: User applies mandatory filters on bus results
    Given User launches the ixigo website
    And User navigates to bus search
    And User enters "Chennai" and "Salem"
    And User selects travel date and clicks search
    When user applies mandatory filters
    Then filtered bus results should be displayed

  
  @optionalFilter
  Scenario: User applies optional price filter on bus results
    Given User launches the ixigo website
    And User navigates to bus search
    When user enters bus search details
      | source  | destination |
      | Chennai | Salem       |
    And User selects travel date and clicks search
    And user applies optional filter "price"
    Then filtered bus results should be displayed

   
     
 @seat
  Scenario: User selects a bus and chooses a seat
    Given User launches the ixigo website
    And User navigates to bus search
    And User enters "Chennai" and "Salem"
    And User selects travel date and clicks search
    And user applies mandatory filters
    When user selects the bus and seat
    And user selects boarding and dropping point
    Then personal details page should be displayed


@passenger
  Scenario: User fills passenger details and proceeds to payment
    Given User launches the ixigo website
    And User navigates to bus search
    And User enters "Chennai" and "Salem"
    And User selects travel date and clicks search
    And user applies mandatory filters
    And user selects the bus and seat
    And user selects boarding and dropping point
    When user enters passenger name and age
    And user clicks proceed to pay
    Then user should be navigated to the payment page