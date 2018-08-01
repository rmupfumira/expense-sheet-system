package expense.sheet.system

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class CurrencyConverterServiceSpec extends Specification implements ServiceUnitTest<CurrencyConverterService>{

    def setup() {

    }

    def cleanup() {
    }


    void testConvertCurrency() {
        given: 'a user'
        def user = new User(name: "Test", initialBankBalance: 1000)
        def expense1 = new Expense(description: "rent", amount: 300, user: user)
        def expense2 = new Expense(description: "food", amount: 300, user: user)

        when: 'service is called'
        def convertedAmount1 = service.convertRandToUSD(expense1.amount)

        then: 'a USD equivalent must be returned'
        assert convertedAmount1 > 0.0

    }


}
