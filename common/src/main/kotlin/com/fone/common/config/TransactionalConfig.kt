package com.fone.common.config

import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.annotation.Configuration
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.startCoroutineUninterceptedOrReturn
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

@Aspect
@Configuration
class TransactionalConfig(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) {
    @Around("@annotation(javax.transaction.Transactional)")
    fun javaxTransaction(joinPoint: ProceedingJoinPoint): Any? {
        return createTransaction(joinPoint)
    }

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun springDataTransaction(joinPoint: ProceedingJoinPoint): Any? {
        return createTransaction(joinPoint)
    }

    private fun createTransaction(joinPoint: ProceedingJoinPoint): Any? {
        // continuation이 없으면 suspend 함수가 아님. 그냥 끝냄.
        if (joinPoint.args.last() !is Continuation<*>) return joinPoint.proceed()
        return joinPoint.runCoroutine {
            queryFactory.transactionWithFactory { _ ->
                joinPoint.proceedCoroutine()
            }
        }
    }
}

private val ProceedingJoinPoint.coroutineContinuation: Continuation<Any?>
    get() = this.args.last() as Continuation<Any?>

private val ProceedingJoinPoint.coroutineArgs: Array<Any?>
    get() = this.args.sliceArray(0 until this.args.size - 1)

private suspend fun ProceedingJoinPoint.proceedCoroutine(
    args: Array<Any?> = this.coroutineArgs,
): Any? =
    suspendCoroutineUninterceptedOrReturn { continuation -> // 마법, continuation -> COROUTINE_SUSPENDED을 돌려주게하려면 이걸 호출해야됨.
        this.proceed(args + continuation)
    }

private fun ProceedingJoinPoint.runCoroutine(
    block: suspend () -> Any?,
): Any? =
    block.startCoroutineUninterceptedOrReturn(this.coroutineContinuation)
