package expense.sheet.system

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ExpenseController implements GrailsConfigurationAware{

    ExpenseService expenseService

    UserService userService

    CurrencyConverterService currencyConverterService

    String csvMimeType

    String encoding

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond expenseService.list(params), model:[expenseCount: expenseService.count()]
    }

    def show(Long id) {
        respond expenseService.get(id)
    }

    def create() {
        respond new Expense(params)
    }

    def save(Expense expense) {
        if (expense == null) {
            notFound()
            return
        }

        try {
            expenseService.save(expense)
        } catch (ValidationException e) {
            respond expense.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'expense.label', default: 'Expense'), expense.id])
                redirect expense
            }
            '*' { respond expense, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond expenseService.get(id)
    }

    def update(Expense expense) {
        if (expense == null) {
            notFound()
            return
        }

        try {
            expenseService.save(expense)
        } catch (ValidationException e) {
            respond expense.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'expense.label', default: 'Expense'), expense.id])
                redirect expense
            }
            '*'{ respond expense, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        expenseService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'expense.label', default: 'Expense'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def statement(Long id) {
        def user = getUserStatement(id)

        respond user
    }

    def downloadCSV(Long id) {
        final String filename = 'statement.csv'
        def user = getUserStatement(id)
        def lines = user.expenses.collect { [it.dateCreated, it.description, it.amount, it.convertedAmount, it.runningBalance].join(';') } as List<String>

        def outs = response.outputStream
        response.status = OK.value()
        response.contentType = "${csvMimeType};charset=${encoding}";
        response.setHeader "Content-disposition", "attachment; filename=${filename}"

        lines.each { String line ->
            outs << "${line}\n"
        }

        outs.flush()
        outs.close()
    }

    protected getUserStatement(Long id){
        def user = userService.get(id)
        user.expenses = user.expenses.sort {it.dateCreated}
        def runningbalance = user.initialBankBalance
        for(expense in user.expenses) {
            expense.runningBalance = runningbalance - expense.amount
            runningbalance = expense.runningBalance
            expense.convertedAmount = currencyConverterService.ZarToUsd(expense.amount)
        }

        return user
    }

    @Override
    void setConfiguration(Config co) {
        csvMimeType = co.getProperty('grails.mime.types.csv', String, 'text/csv')
        encoding = co.getProperty('grails.converters.encoding', String, 'UTF-8')

    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'expense.label', default: 'Expense'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
