package expense.sheet.system

import java.sql.Timestamp

class Expense {

    Date dateCreated
    Double amount
    String description
    Double runningBalance = 0.0

    static transients = ['runningBalance']

    static belongsTo = [user: User]

    static constraints = {
        description blank: false
        amount blank: false
    }

    String toString() {
        return description+" - "+amount
    }
}
