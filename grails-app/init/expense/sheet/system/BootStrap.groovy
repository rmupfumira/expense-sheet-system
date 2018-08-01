package expense.sheet.system

class BootStrap {

    def init = { servletContext ->

        def user = new User(name: "Russel",initialBankBalance: 1000).save()

        def expense1 = new Expense(amount: 300, description: "rent", user: user).save()
        def expense2 = new Expense(amount: 100, description: "fuel", user: user).save()
        def expense3 = new Expense(amount: 200, description: "food", user: user).save()
    }
    def destroy = {
    }
}
