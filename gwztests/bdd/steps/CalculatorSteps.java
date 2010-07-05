package bdd.steps;

import static org.junit.Assert.assertEquals;

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
		assertEquals(result, calculator.showing());
    }
	
	@DomainStep("I subtract the numbers")
	public void subtract() {
		calculator.subtract();
	}
	
    @DomainStep("I mangle with the numbers randomly for (.*) seconds")
    public void playWithNumbers(int times) throws Exception {
        System.out.println("Started big calculation");
        for (int i = 0; i < times; i++) {
            System.out.println("Calculating something difficult, it will take few seconds. Index: " + i);
            Thread.sleep(1000);
        }
        System.out.println("Finished calculating!");
    }

    @DomainStep("Life is much better")
    public void lifeIsGood() {
        System.out.println("Life is good and full of zasadzkas.");
    }
}