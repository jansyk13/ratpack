/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ratpackframework.path;

import java.util.Map;

/**
 * A path binding represents some kind of "match" on the path of a request.
 *
 * @see PathBinder
 */
public interface PathBinding {

  /**
   * The path of the request path that was bound to.
   * <p>
   * Some bindings are "prefix" bindings, in which case they will not have bound to the whole path.
   * The bound path is always absolute.
   * <p>
   * If a prefix binder for path "/a/b/c" created a binding for path "/a/b/c/d/e", the "bound to" value would be "/a/b/c".
   *
   * @return The path of the request path that was bound to.
   */
  String getBoundTo();

  /**
   * The section of the path that is "past" where the binding bound to.
   * <p>
   * Strict bindings may bind to an exact path, therefore there is nothing "past" what they bind to.
   * <p>
   * If a prefix binder for path "/a/b/c" created a binding for path "/a/b/c/d/e", the "past binding" value would be "d/e".
   *
   * @return The part of the path bound to that is past where the binding bound to. May be an empty string if the exact path was bound to.
   */
  String getPastBinding();

  /**
   * Constructs a new path using the {@link #getBoundTo()} value of this binding and the given path.
   * <p>
   * For a binding with a "bound to" value of "/a/b/c", calling join("d/e") will return "/a/b/c/d/e".
   * It is invalid to call this method with an absolute path.
   *
   * @param path The path to use to construct a new child path from the bind point of this binding
   * @return The child path
   */
  String childPath(String path);

  /**
   * Any tokens that the binding has extracted from the path.
   * <p>
   * The definition of how tokens are extracted is implementation specific. Except that tokens are never
   * extracted from the query string, only the path.
   * <p>
   * The returned map is ordered. The order of the map entries denotes the order in which the tokens were extracted.
   *
   * @return Any tokens extracted by the binding.
   * @see #getAllTokens()
   */
  PathTokens getTokens();

  /**
   * Similar to {@link #getTokens()} except that tokens of all parent bindings are included.
   * <p>
   * If a parent binding extracts a token with the same name as this binding, the token from this binding
   * will supersede the value from the parent.
   *
   * @return All tokens extracted from the path by this binding and its parents.
   */
  PathTokens getAllTokens();
}
