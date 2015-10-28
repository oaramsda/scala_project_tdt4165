import exceptions._

class Account(initialBalance: Double, val uid: Long = Bank getUniqueId) {

	var balance : Double = initialBalance

	def withdraw(amount: Double) {
		if (amount >= 0)
			if (amount > balance)
				throw new NoSufficientFundsException
			else
				this.synchronized {
					balance -= amount
				}
		else
			throw new IllegalAmountException
	}

	def deposit(amount: Double) {
		if (amount >= 0)
			this.synchronized {
				balance += amount;
			}
		else
			throw new IllegalAmountException
	}

	def getBalanceAmount: Double = {
		this.synchronized {
			balance
		}
	}
}
