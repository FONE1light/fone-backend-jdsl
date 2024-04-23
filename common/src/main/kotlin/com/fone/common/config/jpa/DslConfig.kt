package com.fone.common.config.jpa

import com.linecorp.kotlinjdsl.query.spec.expression.ExpressionSpec
import com.linecorp.kotlinjdsl.query.spec.expression.LiteralSpec
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.query.spec.predicate.InValueSpec
import com.linecorp.kotlinjdsl.query.spec.predicate.PredicateSpec

class DslConfig

fun <R> ExpressionSpec<R>.inValues(values: Collection<R>): PredicateSpec {
    if (values.isEmpty()) {
        // values가 없으면 항상 false이도록
        return EqualValueSpec(LiteralSpec(1), 0)
    }
    return InValueSpec(this, values)
}
