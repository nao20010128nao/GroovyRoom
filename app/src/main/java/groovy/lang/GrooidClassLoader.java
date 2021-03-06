/*
 *    Copyright 2017 nao20010128nao
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package groovy.lang;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;

import java.security.AccessController;
import java.security.PrivilegedAction;

import groovyjarjarasm.asm.ClassWriter;

/**
 * An extension of {@link groovy.lang.GroovyClassLoader} which handles the fact
 * that classes generated by Groovy will not be directly loadable by the dex
 * class loader.
 *
 * @author Cédric Champeau
 */
public class GrooidClassLoader extends GroovyClassLoader {

    public GrooidClassLoader(ClassLoader loader, CompilerConfiguration config) {
        super(loader, config);
    }

    @Override
    protected ClassCollector createCollector(CompilationUnit unit, SourceUnit su) {
        InnerLoader loader = AccessController.doPrivileged(new PrivilegedAction<InnerLoader>() {
            public InnerLoader run() {
                return new InnerLoader(GrooidClassLoader.this);
            }
        });
        return new ClassCollector(loader, unit, su) {
            @Override
            protected Class onClassNode(ClassWriter classWriter, ClassNode classNode) {
                try {
                    return super.onClassNode(classWriter, classNode);
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
}
