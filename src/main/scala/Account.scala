import exceptions._

class Account(initialBalance: Double, val uid: Int = Bank getUniqueId) {

  def withdraw(amount: Double) {
    if (amount >= 0) {
      if ((initialBalance - amount) < 0) {
        throw new NoSufficientFundsException();
      } else {
        initialBalance -= amount;
      }
    }
    else {
      throw new IllegalAmountException();
    }
  }
  
  def deposit(amount: Double) {
    if (amount >= 0) {
      initialBalance += amount;
    } else {
      throw new IllegalAmountException();
    }
  }
  
  def getBalanceAmount: Unit = {
    this.initialBalance;
  }
}
