/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.puff.assertion.matches;

import com.karuslabs.puff.Texts;
import com.karuslabs.puff.assertion.SkeletonAssertion;
import com.karuslabs.puff.assertion.matches.TypeMatch.Relation;
import com.karuslabs.puff.type.*;

import java.util.function.BiConsumer;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class TypeMatch extends SkeletonAssertion implements Timeable<TypeMirror> {

    public static final Timeable<TypeMirror> ANY = new AnyType();
    
    
    public static Timeable<TypeMirror> is(Class<?> type) {
        var kind = TypeMirrors.kind(type);
        if (kind.isPrimitive()) {
            return is(kind);
                
        } else {
            return new ClassMatch(Relation.IS, type);
        }
    }
    
    public static Timeable<TypeMirror> is(TypeKind primitive) {
        return new PrimitiveMatch(primitive);
    }
    
    public static Timeable<TypeMirror> is(TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return new PrimitiveMatch(type.getKind());
            
        } else {
            return new TypeMirrorMatch(Relation.IS, type);
        }
    }
    
    public static Timeable<TypeMirror> subtype(Class<?>... types) {
        return new ClassMatch(Relation.SUBTYPE, types);
    }
    
    public static Timeable<TypeMirror> subtype(TypeMirror... types) {
        return new TypeMirrorMatch(Relation.SUBTYPE, types);
    }
    
    
    public static Timeable<TypeMirror> supertype(Class<?>... types) {
        return new ClassMatch(Relation.SUPERTYPE, types);
    }
    
    public static Timeable<TypeMirror> supertype(TypeMirror... types) {
        return new TypeMirrorMatch(Relation.SUPERTYPE, types);
    }
    
    
    protected final Relation relation;
    protected final TypeMirror[] mirrors;
    protected final String types;
    
    public TypeMatch(Relation relation, TypeMirror[] mirrors, String types) {
        super(relation.relation() + types, relation.relations() + types);
        this.relation = relation;
        this.mirrors = mirrors;
        this.types = types;
    }
    
    @Override
    public boolean test(TypeMirrors types, Element element) {
        return test(types, element.asType());
    }
    
    @Override
    public String describe(Element element) {
        return describe(element.asType());
    }
    
    @Override
    public String describe(TypeMirror type) {
        return TypePrinter.simple(type);
    }
    
    
    public static abstract class Relation {
    
        public static final Relation IS = new Is();
        public static final Relation SUBTYPE = new Subtype();
        public static final Relation SUPERTYPE = new Supertype();

        boolean test(TypeMirrors types, TypeMirror type, TypeMirror[] expected) {
            for (var mirror : expected) {
                if (!verify(types, type, mirror)) {
                    return false;
                }
            }

            return true;
        }

        public abstract boolean verify(TypeMirrors types, TypeMirror type, TypeMirror expected);

        public abstract String relation();

        public abstract String relations();

    }
    
}

class AnyType extends TypeMatch {

    public AnyType() {
        super(Relation.IS, new TypeMirror[0], "type");
    }

    @Override
    public boolean test(TypeMirrors types, TypeMirror value) {
        return true;
    }
    
    @Override
    public String conditions() {
        return "types";
    }
    
}

class PrimitiveMatch extends SkeletonAssertion implements Timeable<TypeMirror> {

    final TypeKind primitive;
    
    PrimitiveMatch(TypeKind primitive) {
        this(primitive, primitive.toString().toLowerCase());
    }
    
    PrimitiveMatch(TypeKind primitive, String condition) {
        super(condition, condition + "s");
        this.primitive = primitive;
    }
    
    @Override
    public boolean test(TypeMirrors types, Element element) {
        return test(types, element.asType());
    }
    
    @Override
    public boolean test(TypeMirrors types, TypeMirror type) {
        return primitive == type.getKind();
    }

    @Override
    public String describe(Element element) {
        return describe(element.asType());
    }
    
    @Override
    public String describe(TypeMirror type) {
        return TypePrinter.simple(type);
    }
    
}

class ClassMatch extends TypeMatch {

    static final BiConsumer<Class<?>, StringBuilder> FORMAT = (type, builder) -> builder.append(type.getSimpleName());
    
    @Nullable Class<?>[] classes;
    
    ClassMatch(Relation relation, Class<?>... classes) {
        super(relation, new TypeMirror[classes.length], Texts.and(classes, FORMAT));
        this.classes = classes;
    }
    
    @Override
    public boolean test(TypeMirrors types, TypeMirror type) {
        if (classes != null) {
            for (int i = 0; i < classes.length; i++) {
                mirrors[i] = types.type(classes[i]);
            }
            classes = null;
        }
        
        return relation.test(types, type, mirrors);
    }
    
}

class TypeMirrorMatch extends TypeMatch {
    
    static final BiConsumer<TypeMirror, StringBuilder> FORMAT = (type, builder) -> type.accept(TypePrinter.SIMPLE, builder);
    
    TypeMirrorMatch(Relation relation, TypeMirror... mirrors) {
        super(relation, mirrors, Texts.and(mirrors, FORMAT));
    }
    
    @Override
    public boolean test(TypeMirrors types, TypeMirror type) {
        return relation.test(types, type, mirrors);
    }
    
}

class Is extends Relation {

    @Override
    public boolean verify(TypeMirrors types, TypeMirror type, TypeMirror expected) {
        return types.isSameType(type, expected);
    }

    @Override
    public String relation() {
        return "";
    }

    @Override
    public String relations() {
        return "";
    }
    
}

class Subtype extends Relation {
    
    @Override
    public boolean verify(TypeMirrors types, TypeMirror type, TypeMirror expected) {
        return types.isSubtype(type, expected);
    }
    
    @Override
    public String relation() {
        return "subtype of ";
    }

    @Override
    public String relations() {
        return "subtypes of ";
    }
    
}

class Supertype extends Relation {

    @Override
    public boolean verify(TypeMirrors types, TypeMirror type, TypeMirror expected) {
        return types.isSubtype(expected, type);
    }

    @Override
    public String relation() {
        return "supertype of ";
    }

    @Override
    public String relations() {
        return "supertypes of ";
    }
    
}
