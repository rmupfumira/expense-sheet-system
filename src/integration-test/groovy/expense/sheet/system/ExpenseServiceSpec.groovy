package expense.sheet.system

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import javassist.expr.Instanceof
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ExpenseServiceSpec extends Specification {

    ExpenseService expenseService
    SessionFactory sessionFactory


    def setup() {
        sessionFactory.openSession()
    }

    def cleanup() {
        sessionFactory.close()
    }

    private Long setupData() {

        def user = new User(name: "TestingName",initialBankBalance: 1000).save(flush: true, failOnError: true)

        new Expense(amount: 350, description: "food", user: user).save(flush: true, failOnError: true)
        new Expense(amount: 200, description: "fuel", user: user).save(flush: true, failOnError: true)
        Expense expense = new Expense(amount: 300, description: "rent", user: user).save(flush: true, failOnError: true)
        return expense.id
    }

    void "test get"() {
        setupData()

        expect:
        expenseService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Expense> expenseList = expenseService.list(max: 2)

        then:
        expenseList.size() == 2
    }

    void "test count"() {
        setupData()

        expect:
        expenseService.count() == 3
    }

    void "test delete"() {
        Long expenseId = setupData()

        expect:
        expenseService.count() == 3

        when:
        expenseService.delete(expenseId)
        sessionFactory.currentSession.flush()

        then:
        expenseService.count() == 2
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Expense expense = Expense(amount: 300, description: "rent", user: user)
        expenseService.save(expense)

        then:
        expense.id != null
    }
}
