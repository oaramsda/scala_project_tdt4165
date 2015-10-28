

object Main extends App {

	def thread(body: => Unit): Thread = {
		val t = new Thread {
			override def run() = body
		}
		t.start
		t
	}

  	// Write a few transaction examples using Threads
  	val n = 2
  	val accs = for (i <- 0 to n) yield {new Account(Math.round(Math.random*10000): Double)}
  	//accs.foreach{acc => acc.deposit(Math.round(Math.random*1000): Double)}

  	val rand = new java.util.Random(System.nanoTime());
	var random_index = rand.nextInt();
	
	for (i <- 0 to n) {
		random_index = rand.nextInt(accs.length)
		val t = thread(Bank.transaction(accs(random_index), accs(random_index), Math.round(Math.random*500): Double))
	}

  	accs.foreach{acc => println(acc.getBalanceAmount)}
}
