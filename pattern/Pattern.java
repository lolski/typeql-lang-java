/*
 * Copyright (C) 2021 Vaticle
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vaticle.typeql.lang.pattern;

import com.vaticle.typeql.lang.common.exception.TypeQLException;
import com.vaticle.typeql.lang.pattern.variable.BoundVariable;
import com.vaticle.typeql.lang.pattern.variable.UnboundVariable;

import java.util.List;
import java.util.Set;

import static com.vaticle.typedb.common.util.Objects.className;
import static com.vaticle.typeql.lang.common.exception.ErrorMessage.INVALID_CASTING;

public interface Pattern {

    Pattern normalise();

    List<? extends Pattern> patterns();

    void validateIsBoundedBy(Set<UnboundVariable> bounds);

    default boolean isVariable() { return false; }

    default boolean isConjunction() { return false; }

    default boolean isDisjunction() { return false; }

    default boolean isNegation() { return false; }

    default boolean isConjunctable() { return false; }

    default BoundVariable asVariable() {
        throw TypeQLException.of(INVALID_CASTING.message(className(this.getClass()), className(BoundVariable.class)));
    }

    default Conjunction<? extends Pattern> asConjunction() {
        throw TypeQLException.of(INVALID_CASTING.message(className(this.getClass()), className(Conjunction.class)));
    }

    default Disjunction<? extends Pattern> asDisjunction() {
        throw TypeQLException.of(INVALID_CASTING.message(className(this.getClass()), className(Disjunction.class)));
    }

    default Negation<? extends Pattern> asNegation() {
        throw TypeQLException.of(INVALID_CASTING.message(className(this.getClass()), className(Negation.class)));
    }

    default Conjunctable asConjunctable() {
        throw TypeQLException.of(INVALID_CASTING.message(className(this.getClass()), className(Negation.class)));
    }

    @Override
    String toString();
}
