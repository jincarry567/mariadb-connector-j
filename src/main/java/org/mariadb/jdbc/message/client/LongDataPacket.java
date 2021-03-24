/*
 * MariaDB Client for Java
 *
 * Copyright (c) 2012-2014 Monty Program Ab.
 * Copyright (c) 2015-2020 MariaDB Corporation Ab.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to Monty Program Ab info@montyprogram.com.
 *
 */

package org.mariadb.jdbc.message.client;

import java.io.IOException;
import java.sql.SQLException;
import org.mariadb.jdbc.ServerPreparedStatement;
import org.mariadb.jdbc.client.context.Context;
import org.mariadb.jdbc.client.socket.PacketWriter;
import org.mariadb.jdbc.codec.Parameter;

/**
 * COM_STMT_SEND_LONG_DATA
 *
 * <p>Permit to send ONE value in a dedicated packet. The advantage is when length is unknown, to
 * stream easily data to socket
 *
 * <p>https://mariadb.com/kb/en/com_stmt_send_long_data/
 */
public final class LongDataPacket implements ClientMessage {

  private final int statementId;
  private final Parameter<?> parameter;
  private final int index;
  private final String command;
  private final ServerPreparedStatement prep;

  public LongDataPacket(
      int statementId,
      Parameter<?> parameter,
      int index,
      String command,
      ServerPreparedStatement prep) {
    this.statementId = statementId;
    this.parameter = parameter;
    this.index = index;
    this.command = command;
    this.prep = prep;
  }

  @Override
  public int encode(PacketWriter writer, Context context) throws IOException, SQLException {
    writer.initPacket();
    writer.writeByte(0x18);
    writer.writeInt(statementId);
    writer.writeShort((short) index);
    parameter.encodeLongData(writer, context);
    writer.flush();
    return 0;
  }
}