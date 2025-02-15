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

package com.vaticle.typeql.lang.test.deployment.src.test.java;

import com.vaticle.typeql.lang.TypeQL;
import com.vaticle.typeql.lang.query.TypeQLMatch;
import com.vaticle.typeql.lang.query.TypeQLQuery;
import org.junit.Test;

import static com.vaticle.typeql.lang.TypeQL.and;
import static com.vaticle.typeql.lang.TypeQL.match;
import static com.vaticle.typeql.lang.TypeQL.or;
import static com.vaticle.typeql.lang.TypeQL.rel;
import static com.vaticle.typeql.lang.TypeQL.var;
import static org.junit.Assert.assertEquals;

public class MavenApplicationTest {
    private void assertQueryEquals(TypeQLQuery expected, TypeQLQuery parsed, String query) {
        assertEquals(expected, parsed);
        assertEquals(expected, TypeQL.parseQuery(parsed.toString()));
        assertEquals(query, expected.toString());
    }

    @Test
    public void testRelationQuery() {
        final String query = "match\n" +
                "$brando 'Marl B' isa name;\n" +
                "(actor: $brando, $char, production-with-cast: $prod);\n" +
                "get $char, $prod;";
        TypeQLMatch parsed = TypeQL.parseQuery(query).asMatch();

        TypeQLMatch expected = match(
                var("brando").eq("Marl B").isa("name"),
                rel("actor", "brando").rel("char").rel("production-with-cast", "prod")
        ).get("char", "prod");

        assertQueryEquals(expected, parsed, query.replace("'", "\""));
    }

    @Test
    public void testPredicateQuery1() {
        final String query = "match\n" +
                "$x isa movie, has title $t;\n" +
                "{ $t 'Apocalypse Now'; } or { $t < 'Juno'; $t > 'Godfather'; } or { $t 'Spy'; };\n" +
                "$t != 'Apocalypse Now';";
        TypeQLMatch parsed = TypeQL.parseQuery(query).asMatch();

        TypeQLMatch expected = match(
                var("x").isa("movie").has("title", var("t")),
                or(
                        var("t").eq("Apocalypse Now"),
                        and(
                                var("t").lt("Juno"),
                                var("t").gt("Godfather")
                        ),
                        var("t").eq("Spy")
                ),
                var("t").neq("Apocalypse Now")
        );

        assertQueryEquals(expected, parsed, query.replace("'", "\""));
    }
}
