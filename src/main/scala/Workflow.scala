object WorkflowFactory {
  def create(command: Command[_]): Workflow =
    command match {
      case c: ApplyForCVRegistrationCommand => ApplyForCVRegistrationWorkflow(c)
    }
}

trait Workflow {
  def execute(): Unit
}

case class ApplyForCVRegistrationWorkflow(
    initialCommand: ApplyForCVRegistrationCommand
) extends Workflow {
  def execute(): Unit = {
    println("----- Workflow start -----")
    initialCommand.execute()
    println("----- Workflow end -----")
  }
}

object ApplyForCVRegistrationWorkflow {
  def apply(command: ApplyForCVRegistrationCommand) =
    new ApplyForCVRegistrationWorkflow(command)
}
