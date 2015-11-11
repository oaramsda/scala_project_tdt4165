import java.util.NoSuchElementException

import akka.actor._
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration._
import akka.util.Timeout

case class GetAccountRequest(accountId: String)

case class CreateAccountRequest(initialBalance: Double)

case class IdentifyActor()

class Bank(val bankId: String) extends Actor {

	val accountCounter = new AtomicInteger(1000)

	def createAccount(initialBalance: Double): ActorRef = {
		// Should create a new Account Actor and return its actor reference. Accounts should be assigned with unique ids (increment with 1).
    val new_id: Int = accountCounter.incrementAndGet
    BankManager.createAccount(new_id.toString, bankId, initialBalance)
	}

	def findAccount(accountId: String): Option[ActorRef] = {
		// Use BankManager to look up an account with ID accountId
		Some(BankManager.findAccount(bankId, accountId))
	}

	def findOtherBank(bankId: String): Option[ActorRef] = {
		// Use BankManager to look up a different bank with ID bankId
		Some(BankManager.findBank(bankId))
	}

	override def receive = {
		case CreateAccountRequest(initialBalance) => sender ! createAccount(initialBalance)
		case GetAccountRequest(id) => sender ! findAccount(id)
		case IdentifyActor => sender ! this
		case t: Transaction => processTransaction(t)
		case t: TransactionRequestReceipt => {
			val acc = t.toAccountNumber
			var isInternal = acc.length <= 4
			val toBankId = if (isInternal) bankId else acc.substring(0, 4)
			val toAccountId = if (isInternal) acc else acc.substring(4)
			if (toBankId == bankId)
				isInternal = true

			println("LOOPING?!")

			if (isInternal) {
				val receivingAccount = findAccount(toAccountId).get
				receivingAccount ! t
			} else {
				val sendToExternalBank = findOtherBank(toBankId).get
				sendToExternalBank ! t
			}

		}

		case msg => ???
	}

	def processTransaction(t: Transaction): Unit = {
		implicit val timeout = new Timeout(5 seconds)
		var isInternal = t.to.length <= 4
		val toBankId = if (isInternal) bankId else t.to.substring(0, 4)
		val toAccountId = if (isInternal) t.to else t.to.substring(4)
		val transactionStatus = t.status

		if (toBankId == bankId)
			isInternal = true

		println(toBankId + " " + bankId)

		if (isInternal) {
			println("IS INTERNAL")
			val receivingAccount = findAccount(toAccountId)
			if (receivingAccount.isEmpty) {
				t.status = TransactionStatus.FAILED
				this.self ! new TransactionRequestReceipt(toAccountId, t.id, t)
			}
			else {
				println("ACCOUNT RECEIVED TRANSACTION")
				receivingAccount.get ! t
			}
    } else {
      val sendToExternalBank = findOtherBank(toBankId)
			if (sendToExternalBank.isEmpty) {
				t.status = TransactionStatus.FAILED
				this.self ! new TransactionRequestReceipt(toAccountId, t.id, t)
			}
			else
				sendToExternalBank.get ! t
    }

		// This method should forward Transaction t to an account or another bank, depending on the "to"-address.
		// HINT: Make use of the variables that have been defined above.
	}
}
