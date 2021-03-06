/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.mozzes.application.server.mockups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerService1Impl implements ServerService1 {

  private static final Logger logger = LoggerFactory.getLogger(ServerService1Impl.class);

  @Override
  public void service1Method1() {
    logger.info("service1Method1");
  }

  @Override
  public Integer service1Method2() {
    logger.info("service1Method2");
    return Integer.valueOf(123);
  }

  @Override
  public String service1Method3() {
    logger.info("service1Method3");
    return "return value service1Method3";
  }
}
