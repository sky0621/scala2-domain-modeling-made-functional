package com.example.cv.design

import cats.data.EitherT
import com.example.cv.design.Command.{NotifyCVRegistrationResult, SaveApplyForCVRegistrationCommand, VerifyCVRegistrationCommand}
import com.example.cv.design.Workflow.ApplyForCVRegistration
import com.example.cv.implementation.Workflow.{ApplyForCVRegistrationInput, ApplyForCVRegistrationOutput}
import com.example.cv.implementation.domain.CompoundModel.{UnvalidatedApplyForCVRegistration, ValidatedApplyForCVRegistration}
import com.example.cv.implementation.domain.{DomainError, NotifiedCVRegistrationEvent, SavedApplyForCVRegistrationEvent, ValidatedCVRegistrationEvent}

object Design {
  /*
   * CV登録を申請する
   *
   * 氏名、生年月日、メールアドレスを受け取って、CV登録を申請する。
   * ・申請情報を保存する。
   * ・申請情報を検証する。
   * ・申請結果を通知する。
   */
  // @formatter:off
  type ApplyForCVRegistrationWorkflow[F[_]] =
    SaveApplyForCVRegistrationCommand[F] =>
    VerifyCVRegistrationCommand[F] =>
    NotifyCVRegistrationResult[F] =>
      ApplyForCVRegistration[F]
  // @formatter:on

}

// @formatter:off
object Workflow {
  type ApplyForCVRegistration[F[_]] = ApplyForCVRegistrationInput => EitherT[F, DomainError, ApplyForCVRegistrationOutput]
}
// @formatter:on

// @formatter:off
object Command {
  // CV登録申請情報を保存する
  type SaveApplyForCVRegistrationCommand[F[_]] = UnvalidatedApplyForCVRegistration => EitherT[F, DomainError, SavedApplyForCVRegistrationEvent]

  // CV登録申請内容を検証する
  type VerifyCVRegistrationCommand[F[_]] = UnvalidatedApplyForCVRegistration => EitherT[F, DomainError, ValidatedCVRegistrationEvent]

  // CV登録申請結果を通知する
  type NotifyCVRegistrationResult[F[_]] = ValidatedApplyForCVRegistration => EitherT[F, DomainError, NotifiedCVRegistrationEvent]
}
// @formatter:on

object Repository {
  //
}

object EventHistoryRepository {
  type Save[F[_]] = (String, Long) => EitherT[F, DomainError, Unit]
}

object Infra {
  //
}
