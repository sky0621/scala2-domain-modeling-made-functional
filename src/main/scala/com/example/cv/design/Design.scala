package com.example.cv.design

import cats.data.EitherT
import com.example.cv.design.CVRegistration.{NotifyResult, SaveUnvalidatedApply, Validate}
import com.example.cv.implementation.domain.CompoundModel.{UnvalidatedApplyForCVRegistration, ValidatedApplyForCVRegistration}
import com.example.cv.implementation.domain.DomainError
import com.example.cv.implementation.domain.Model.{Token, TokenLength}
import com.example.cv.implementation.domain.cvregistration.CVRegistrationEvent

object Workflow {

  /*
   * CV登録を申請する
   *
   * 氏名、生年月日、メールアドレスを受け取って、CV登録を申請する。
   * ・申請情報を保存する。
   * ・申請情報を検証する。
   * ・申請結果を通知する。
   */
  // @formatter:off
  type ApplyForCVRegistrationWorkflow[F[_]] = (
    SaveUnvalidatedApply[F],
    Validate[F],
    NotifyResult[F],
  ) =>
    UnvalidatedApplyForCVRegistration => EitherT[F, DomainError, Seq[CVRegistrationEvent]]
  // @formatter:on
}

// @formatter:off
object CVRegistration {
  // CV登録申請情報を保存する
  type SaveUnvalidatedApply[F[_]] =
    UnvalidatedApplyForCVRegistration => EitherT[F, DomainError, Unit]

  // CV登録申請内容を検証する
  type Validate[F[_]] =
    UnvalidatedApplyForCVRegistration => EitherT[F, DomainError, ValidatedApplyForCVRegistration]

  // CV登録申請結果を通知する
  type NotifyResult[F[_]] =
    (ValidatedApplyForCVRegistration, Token) => EitherT[F, DomainError, Unit]
}
// @formatter:on

object Service {
  // 指定の長さのトークンを生成する
  type GenerateToken = TokenLength => Token
}
