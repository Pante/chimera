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

package com.karuslabs.puff.type;

import com.karuslabs.puff.mock.MockName;

import java.util.List;
import javax.lang.model.element.*;
import javax.lang.model.type.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypePrinterTest {

    TypePrinter printer = spy(new StubTypePrinter());
    StringBuilder builder = new StringBuilder();
    
    DeclaredType declared = mock(DeclaredType.class);
    
    TypeVariable variable = type(TypeVariable.class, "variable");
    TypeMirror upper = type("upper");
    TypeMirror lower = type("lower");
    
    
    TypeMirror type(String message) {
        return type(TypeMirror.class, message);
    }
    
    <T extends TypeMirror> T type(Class<T> type, String message) {
        return when(mock(type).accept(printer, builder)).then(invocation -> 
            invocation.getArgument(1, StringBuilder.class).append(message)
        ).getMock();
    }
    
    
    @Test
    void visitDeclared() {
        when(declared.getTypeArguments()).thenReturn(List.of());
        
        printer.visitDeclared(declared, builder);
        assertEquals("declared", builder.toString());
    }
    
    @Test
    void visitDeclared_type_variables() {
        doReturn(List.of(variable, variable)).when(declared).getTypeArguments();
        
        printer.visitDeclared(declared, builder);
        
        assertEquals("declared<variable, variable>", builder.toString());
    }
    
    
    @Test
    void visitTypeVariable_upper() {
        Element element = when(mock(Element.class).getSimpleName()).thenReturn(new MockName("a")).getMock();
        when(variable.asElement()).thenReturn(element);
        
        when(lower.getKind()).thenReturn(TypeKind.NULL);
        
        when(variable.getUpperBound()).thenReturn(upper);
        when(variable.getLowerBound()).thenReturn(lower);
        
        
        try (var scope = mockStatic(TypeMirrors.class)) {
            scope.when(() -> TypeMirrors.is(upper, Object.class)).thenReturn(false);
            
            printer.visitTypeVariable(variable, builder);
            assertEquals("a extends upper", builder.toString());
        }
    }
    
    @Test
    void visitTypeVariable_lower() {
        Element element = when(mock(Element.class).getSimpleName()).thenReturn(new MockName("a")).getMock();
        when(variable.asElement()).thenReturn(element);
        
        when(lower.getKind()).thenReturn(TypeKind.DECLARED);
        
        when(variable.getUpperBound()).thenReturn(upper);
        when(variable.getLowerBound()).thenReturn(lower);
        
        
        try (var scope = mockStatic(TypeMirrors.class)) {
            scope.when(() -> TypeMirrors.is(upper, Object.class)).thenReturn(true);
            
            printer.visitTypeVariable(variable, builder);
            assertEquals("a super lower", builder.toString());
        }
    }
    
    
    @Test
    void visitWildcard_extends() {
        var extension = type("other");
        WildcardType wildcard = when(mock(WildcardType.class).getExtendsBound()).thenReturn(extension).getMock();
        printer.visitWildcard(wildcard, builder);
        assertEquals("? extends other", builder.toString());
    }
    
    @Test
    void visitWildcard_super() {
        var superBound = type("other");
        WildcardType wildcard = when(mock(WildcardType.class).getSuperBound()).thenReturn(superBound).getMock();
        printer.visitWildcard(wildcard, builder);
        assertEquals("? super other", builder.toString());
    }
    
    
    @Test
    void visitIntersection() {
        IntersectionType intersection = mock(IntersectionType.class);
        
        var a = type("a");
        var b = type("b");
        doReturn(List.of(a, b)).when(intersection).getBounds();
        
        printer.visitIntersection(intersection, builder);
        
        assertEquals("a & b", builder.toString());
    }
    
    @Test
    void visitIntersection_null() {
        IntersectionType intersection = when(mock(IntersectionType.class).getBounds()).thenReturn(List.of()).getMock();
        printer.visitIntersection(intersection, builder);
        assertEquals("", builder.toString());      
    }
    
    
    @Test
    void visitArray() {
        var type = type("type");
        ArrayType array = when(mock(ArrayType.class).getComponentType()).thenReturn(type).getMock();
        
        printer.visitArray(array, builder);
        
        assertEquals("type[]",builder.toString());
    }
    
    @Test
    void visitPrimitive() {
        PrimitiveType primitive = when(mock(PrimitiveType.class).getKind()).thenReturn(TypeKind.BOOLEAN).getMock();
        printer.visitPrimitive(primitive, builder);
        assertEquals("boolean", builder.toString());
    }
    
    @Test
    void visitNoType() {
        printer.visitNoType(mock(NoType.class), builder);
        assertEquals("void", builder.toString());
    }
    
    @Test
    void defaultAction() {
        TypeMirror type = when(mock(TypeMirror.class).getKind()).thenReturn(TypeKind.EXECUTABLE).getMock();
        
        assertEquals(
            "TypePrinter does not support EXECUTABLE",
            assertThrows(UnsupportedOperationException.class, () -> printer.defaultAction(type, builder)).getMessage()
        );
    }

}

class StubTypePrinter extends TypePrinter {
    @Override
    protected void rawType(DeclaredType type, StringBuilder builder) {

        builder.append("declared");
    }
}

class QualifiedTypePrinterTest {
    
    @Test
    void rawType() {        
        TypeElement element = when(mock(TypeElement.class).getQualifiedName()).thenReturn(new MockName("a.b.c.type")).getMock();
        DeclaredType type = when(mock(DeclaredType.class).asElement()).thenReturn(element).getMock();
        when(type.getTypeArguments()).thenReturn(List.of());
        when(type.accept(any(), any())).then((invocation) -> TypePrinter.QUALIFIED.visitDeclared(type, invocation.getArgument(1, StringBuilder.class)));
  
        assertEquals("a.b.c.type", TypePrinter.qualified(type));
    }
    
    @Test
    void rawType_throws_exception() {
        Element element = mock(Element.class);
        DeclaredType type = when(mock(DeclaredType.class).asElement()).thenReturn(element).getMock();
        
        assertEquals(
            "DeclaredType should be a TypeElement",
            assertThrows(IllegalStateException.class, () -> TypePrinter.QUALIFIED.rawType(type, new StringBuilder())).getMessage()
        );
    }
    
}

class SimpleTypePrinterTest {
    
    PackageElement pack = mock(PackageElement.class);
    TypeElement element = when(mock(TypeElement.class).getQualifiedName()).thenReturn(new MockName("a.b.c.type")).getMock();
    DeclaredType type = when(mock(DeclaredType.class).asElement()).thenReturn(element).getMock();
    
    @BeforeEach
    void before() {
        when(element.accept(any(), any())).thenReturn(pack);
        when(type.accept(any(), any())).then((invocation) -> TypePrinter.SIMPLE.visitDeclared(type, invocation.getArgument(1, StringBuilder.class)));
    }
    
    @Test
    void rawType_default_package() {
        when(pack.getQualifiedName()).thenReturn(new MockName(""));
        
        assertEquals("a.b.c.type", TypePrinter.simple(type));
    }
    
    @Test
    void rawType_package() {
        when(pack.getQualifiedName()).thenReturn(new MockName("a.b.c"));
        
        assertEquals("type", TypePrinter.simple(type));
    }
    
    @Test
    void rawType_throws_exception() {
        Element element = mock(Element.class);
        DeclaredType type = when(mock(DeclaredType.class).asElement()).thenReturn(element).getMock();
        
        assertEquals(
            "DeclaredType should be a TypeElement",
            assertThrows(IllegalStateException.class, () -> TypePrinter.SIMPLE.rawType(type, new StringBuilder())).getMessage()
        );
    }
    
}