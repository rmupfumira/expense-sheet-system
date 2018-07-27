package expense.sheet.system

import java.sql.Timestamp

class Expense {

    Date dateCreated
    Double amount
    String description

    static constraints = {
        description blank: false
        amount blank: false
    }
}
