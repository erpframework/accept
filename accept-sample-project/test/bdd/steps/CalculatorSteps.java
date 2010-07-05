package bdd.steps;

import static org.junit.Assert.assertEquals;

import org.calculator.Calculator;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class CalculatorSteps {
	
	Calculator calculator = new Calculator();
	
	@DomainStep("I enter (.*)")
	public void enterNumber(int number) {
		calculator.enter(number);
	}
	
	@DomainStep("I add the numbers")
    public void add() {
        calculator.add();
    }
	
	@DomainStep("The calculator shows (.*)")
    public void checkResult(String result) {
		//Use any xUnit framework of your choice. 
		//I use jUnit here but in real life use FEST-assert ;)
		assertEquals(result, calculator.showing());
    }
	
	@DomainStep("I subtract the numbers")
	public void subtract() {
		calculator.subtract();
	}
}