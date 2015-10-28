object Main extends App {

	def thread(body: => Unit): Thread = {
		val t = new Thread {
			override def run() = body
		}
		t.start
		t
	}

  	// Write a few transaction examples using Threads
  	val n = 10
  	val accs = for (i <- 0 to n) yield {new Account(1000.0)}
  	//accs.foreach{acc => acc.deposit(Math.round(Math.random*1000): Double)}
	
	for (i <- 0 to n) {
		val t = thread(Bank.transaction(acc1, acc2, 500))
	}

  	
}
