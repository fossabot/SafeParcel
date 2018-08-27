/*
 * Copyright (C) 2013-2017 microG Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.microg.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@SuppressWarnings("MagicNumber")
public final class SafeParcelWriter {

    private SafeParcelWriter() {
    }

    private static void writeStart(Parcel parcel, int position, int length) {
        if (length >= 0xFFFF) {
            parcel.writeInt(0xFFFF0000 | position);
            parcel.writeInt(length);
        } else {
            parcel.writeInt(length << 16 | position);
        }
    }

    public static int writeStart(Parcel parcel) {
        return writeStart(parcel, SafeParcelable.SAFE_PARCEL_MAGIC);
    }

    private static int writeStart(Parcel parcel, int position) {
        parcel.writeInt(0xFFFF0000 | position);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    public static void writeEnd(Parcel parcel, int start) {
        int end = parcel.dataPosition();
        int length = end - start;
        parcel.setDataPosition(start - 4);
        parcel.writeInt(length);
        parcel.setDataPosition(end);
    }

    public static void write(Parcel parcel, int position, Boolean val) {
        if (val == null) return;
        writeStart(parcel, position, 4);
        parcel.writeInt(val ? 1 : 0);
    }

    public static void write(Parcel parcel, int position, Byte val) {
        if (val == null) return;
        writeStart(parcel, position, 4);
        parcel.writeInt(val);
    }

    public static void write(Parcel parcel, int position, Short val) {
        if (val == null) return;
        writeStart(parcel, position, 4);
        parcel.writeInt(val);
    }

    public static void write(Parcel parcel, int position, Integer val) {
        if (val == null) return;
        writeStart(parcel, position, 4);
        parcel.writeInt(val);
    }

    public static void write(Parcel parcel, int position, Long val) {
        if (val == null) return;
        writeStart(parcel, position, 8);
        parcel.writeLong(val);
    }

    public static void write(Parcel parcel, int position, Float val) {
        if (val == null) return;
        writeStart(parcel, position, 4);
        parcel.writeFloat(val);
    }

    public static void write(Parcel parcel, int position, Double val) {
        if (val == null) return;
        writeStart(parcel, position, 8);
        parcel.writeDouble(val);
    }

    public static void write(Parcel parcel, int position, String val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeString(val);
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, Parcelable val, int flags, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            val.writeToParcel(parcel, flags);
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, Bundle val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeBundle(val);
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, byte[] val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeByteArray(val);
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, int[] val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeIntArray(val);
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, String[] val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeStringArray(val);
            writeEnd(parcel, start);
        }
    }

    public static void writeStringList(Parcel parcel, int position, List<String> val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeStringList(val);
            writeEnd(parcel, start);
        }
    }

    private static <T extends Parcelable> void writeArrayPart(Parcel parcel, T val, int flags) {
        int before = parcel.dataPosition();
        parcel.writeInt(1);
        int start = parcel.dataPosition();
        val.writeToParcel(parcel, flags);
        int end = parcel.dataPosition();
        parcel.setDataPosition(before);
        parcel.writeInt(end - start);
        parcel.setDataPosition(end);
    }

    public static <T extends Parcelable> void write(Parcel parcel, int position, T[] val, int flags, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeInt(val.length);
            for (T t : val) {
                if (t == null) {
                    parcel.writeInt(0);
                } else {
                    writeArrayPart(parcel, t, flags);
                }
            }
            writeEnd(parcel, start);
        }
    }

    public static <T extends Parcelable> void write(Parcel parcel, int position, List<T> val, int flags, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeInt(val.size());
            for (T t : val) {
                if (t == null) {
                    parcel.writeInt(0);
                } else {
                    writeArrayPart(parcel, t, flags);
                }
            }
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, Parcel val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.appendFrom(val, 0, val.dataSize());
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, List val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeList(val);
            writeEnd(parcel, start);
        }
    }

    public static void write(Parcel parcel, int position, IBinder val, boolean mayNull) {
        if (val == null) {
            if (mayNull) {
                writeStart(parcel, position, 0);
            }
        } else {
            int start = writeStart(parcel, position);
            parcel.writeStrongBinder(val);
            writeEnd(parcel, start);
        }
    }

}
