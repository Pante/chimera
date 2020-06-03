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
package com.karuslabs.scribe.core.parsers;

import com.karuslabs.annotations.*;
import com.karuslabs.scribe.core.Environment;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * A parser that transforms an annotation into parts of a {@code plugin.yml}.
 * 
 * @param <T> the annotated type
 */
public abstract class Parser<T> {

    /**
     * A pattern that command aliases and names must adhere to.
     */
    public static final Pattern COMMAND = Pattern.compile("(.*\\s+.*)");
    /**
     * A pattern that permissions should adhere to.
     */
    public static final Pattern PERMISSION = Pattern.compile("\\w+(\\.\\w+)*(.\\*)?");
    /**
     * A pattern corresponding to SemVer 2.0.0 that versions should adhere.
     */
    public static final Pattern VERSIONING = Pattern.compile("(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-[a-zA-Z\\d][-a-zA-Z.\\d]*)?(\\+[a-zA-Z\\d][-a-zA-Z.\\d]*)?$");
    /**
     * A pattern that URLs must adhere to.
     */
    public static final Pattern URL = Pattern.compile("(?i)\\b((?:https?:(?:/{1,3}|[a-z0-9%])|[a-z0-9.\\-]+[.](?:com|net|org|edu|gov|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|post|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|Ja|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)/)(?:[^\\s()<>{}\\[\\]]+|\\([^\\s()]*?\\([^\\s()]+\\)[^\\s()]*?\\)|\\([^\\s]+?\\))+(?:\\([^\\s()]*?\\([^\\s()]+\\)[^\\s()]*?\\)|\\([^\\s]+?\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’])|(?:(?<!@)[a-z0-9]+(?:[.\\-][a-z0-9]+)*[.](?:com|net|org|edu|gov|mil|aero|asia|biz|cat|coop|info|int|jobs|mobi|museum|name|post|pro|tel|travel|xxx|ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|Ja|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zw)\\b/?(?!@)))");
    /**
     * A pattern that represents a word.
     */
    public static final Pattern WORD = Pattern.compile("\\w+");
    
    
    /**
     * The current environment.
     */
    protected Environment<T> environment;
    /**
     * The supported annotations.
     */
    protected @Immutable Set<Class<? extends Annotation>> annotations;
    
    
    /**
     * Creates a {@code Parser} with the given parameters.
     * 
     * @param environment the environment
     * @param annotations the supported annotations
     */
    public Parser(Environment<T> environment, @Immutable Set<Class<? extends Annotation>> annotations) {
        this.environment = environment;
        this.annotations = annotations;
    }
    
    
    /**
     * Processes and adds the annotations on {@code types} to {@code environment}.
     * <br>
     * <br>
     * <b>Default implementation: </b>
     * <br>
     * Delegates validation of elements to {@link #check(Set)}. If all elements are 
     * valid, delegates processing of each element to {@link #parse(Object)}.
     * After which, delegates cleaning up to {@link #clear()}.
     * 
     * @param types the annotated types to be resolved
     */
    public void parse(Set<T> types) {
        check(types);
        for (var type : types) {
            parse(type);
        }
        
        clear();
    }
    
    /**
     * Validates the given types.
     * <br>
     * <br>
     * <b>Default implementation: </b>
     * <br>
     * Does nothing.
     * 
     * @param types the annotated types to be validated
     */
    @VisibleForOverride
    protected void check(Set<T> types) {}
  
    /**
     * Processes and adds the annotations on {@code type} to {@code environnment}.
     * 
     * @param type the annotated type
     */
    protected abstract void parse(T type);     
    
    /**
     * Releases acquired resources and resets this resolver to its initial state.
     * <br>
     * <br>
     * <b>Default implementation: </b>
     * <br>
     * Does nothing.
     */
    @VisibleForOverride
    protected void clear() {}
    
    
    /**
     * Returns the annotations supported this {@code Resolver}.
     * 
     * @return the annotations
     */
    public Set<Class<? extends Annotation>> annotations() {
        return annotations;
    }
    
}
