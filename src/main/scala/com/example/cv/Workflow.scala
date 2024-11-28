package com.example.cv

object WorkflowFactory {
  def create[F[_]](command: Command[F, _]): Workflow =
    command match {
      case c: ApplyForCVRegistrationCommand[F] =>
        ApplyForCVRegistrationWorkflow(c)
    }
}

trait Workflow {
  def execute(): Unit
}

case class ApplyForCVRegistrationWorkflow[F[_]](
    applyForCVRegistration: ApplyForCVRegistrationCommand[F]
) extends Workflow {
  def execute(): Unit = {
    println("----- Workflow start -----")
    applyForCVRegistration.execute()
    println("----- Workflow end -----")
  }
}
