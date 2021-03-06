/*
 * Copyright (C) 2016 Christian Stein
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.sormuras.listing;

import java.lang.annotation.ElementType;

/**
 * The variables of a class type are introduced by field declarations.
 *
 * @author Christian Stein
 * @see https://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.3
 */
public class FieldDeclaration extends ClassMemberDeclaration<FieldDeclaration> {

  private Listable initializer = null;
  private JavaType<?> type;

  @Override
  public Listing apply(Listing listing) {
    listing.newline();
    listing.add(toAnnotationsListable());
    listing.add(toModifiersListable());
    listing.add(getType());
    listing.add(' ');
    listing.add(getName());
    if (initializer != null) {
      listing.add(" = ").add(getInitializer());
    }
    listing.add(';').newline();
    return listing;
  }

  @Override
  public ElementType getAnnotationTarget() {
    return ElementType.FIELD;
  }

  public Listable getInitializer() {
    return initializer;
  }

  public JavaType<?> getType() {
    return type;
  }

  public FieldDeclaration setInitializer(Listable initializer) {
    this.initializer = initializer;
    return this;
  }

  public FieldDeclaration setType(JavaType<?> type) {
    this.type = type;
    return this;
  }
}
