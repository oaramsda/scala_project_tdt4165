import java.util.concurrent.atomic.AtomicLong

object Bank {

	private var idCounter: AtomicLong = new AtomicLong

	def transaction(from: Account, to: Account, amount: Double): Unit = {
		if (amount > 0) {
			from.withdraw(amount);
			to.deposit(amount);
		}
	}

	def getUniqueId: Long = {
		idCounter.incrementAndGet
	}

}
