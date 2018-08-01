package expense.sheet.system

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class UserServiceSpec extends Specification {

    UserService userService
    SessionFactory sessionFactory

    def setup() {
        sessionFactory.openSession()
    }

    def cleanup() {
        sessionFactory.close()
    }

    private Long setupData() {
        def user = new User(name: "TestingName",initialBankBalance: 1000).save(flush: true, failOnError: true)
        user.id
    }

    void "test get"() {
        setupData()

        expect:
        userService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<User> userList = userService.list(max: 1, offset: 0)

        then:
        userList.size() == 1
    }

    void "test count"() {
        setupData()

        expect:
        userService.count() == 1
    }

    void "test delete"() {
        Long userId = setupData()

        expect:
        userService.count() == 1

        when:
        userService.delete(userId)
        sessionFactory.currentSession.flush()

        then:
        userService.count() == 0
    }

    void "test save"() {
        when:
        def user = new User(name: "TestingNameAgain",initialBankBalance: 2000).save(flush: true, failOnError: true)
        userService.save(user)

        then:
        user.id != null
    }
}
