object Main {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: Main <commandName> <parameter1> [parameter2 ...]")
      sys.exit(1)
    }

    val commandName = args(0)
    val parameters = args.drop(1)



    val command = CommandFactory.create(commandName, parameters)
    command match {
      case Some(c) => WorkflowFactory.create(c).execute()
      case None    => sys.exit(1)
    }
  }
}
