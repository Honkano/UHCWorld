/*
 * Decompiled with CFR 0.152.
 */
package com.sk89q.worldedit.blocks;

import com.sk89q.worldedit.CuboidClipboard;

public final class BlockData {
    private BlockData() {
    }

    public static int rotate90(int type, int data) {
        switch (type) {
            case 50: 
            case 75: 
            case 76: {
                switch (data) {
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 4;
                    }
                    case 3: {
                        return 2;
                    }
                    case 4: {
                        return 1;
                    }
                }
                break;
            }
            case 66: {
                switch (data) {
                    case 6: {
                        return 7;
                    }
                    case 7: {
                        return 8;
                    }
                    case 8: {
                        return 9;
                    }
                    case 9: {
                        return 6;
                    }
                }
            }
            case 27: 
            case 28: 
            case 157: {
                switch (data & 7) {
                    case 0: {
                        return 1 | data & 0xFFFFFFF8;
                    }
                    case 1: {
                        return 0 | data & 0xFFFFFFF8;
                    }
                    case 2: {
                        return 5 | data & 0xFFFFFFF8;
                    }
                    case 3: {
                        return 4 | data & 0xFFFFFFF8;
                    }
                    case 4: {
                        return 2 | data & 0xFFFFFFF8;
                    }
                    case 5: {
                        return 3 | data & 0xFFFFFFF8;
                    }
                }
                break;
            }
            case 53: 
            case 67: 
            case 108: 
            case 109: 
            case 114: 
            case 128: 
            case 134: 
            case 135: 
            case 136: 
            case 156: 
            case 163: 
            case 164: {
                switch (data) {
                    case 0: {
                        return 2;
                    }
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 1;
                    }
                    case 3: {
                        return 0;
                    }
                    case 4: {
                        return 6;
                    }
                    case 5: {
                        return 7;
                    }
                    case 6: {
                        return 5;
                    }
                    case 7: {
                        return 4;
                    }
                }
                break;
            }
            case 77: 
            case 143: {
                int thrown = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 1: {
                        return 3 | thrown;
                    }
                    case 2: {
                        return 4 | thrown;
                    }
                    case 3: {
                        return 2 | thrown;
                    }
                    case 4: {
                        return 1 | thrown;
                    }
                }
                break;
            }
            case 69: {
                int thrown = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 1: {
                        return 3 | thrown;
                    }
                    case 2: {
                        return 4 | thrown;
                    }
                    case 3: {
                        return 2 | thrown;
                    }
                    case 4: {
                        return 1 | thrown;
                    }
                    case 5: {
                        return 6 | thrown;
                    }
                    case 6: {
                        return 5 | thrown;
                    }
                    case 7: {
                        return 0 | thrown;
                    }
                    case 0: {
                        return 7 | thrown;
                    }
                }
                break;
            }
            case 64: 
            case 71: {
                if ((data & 8) != 0) break;
            }
            case 127: 
            case 131: {
                int extra = data & 0xFFFFFFFC;
                int withoutFlags = data & 3;
                switch (withoutFlags) {
                    case 0: {
                        return 1 | extra;
                    }
                    case 1: {
                        return 2 | extra;
                    }
                    case 2: {
                        return 3 | extra;
                    }
                    case 3: {
                        return 0 | extra;
                    }
                }
                break;
            }
            case 63: {
                return (data + 4) % 16;
            }
            case 54: 
            case 61: 
            case 62: 
            case 65: 
            case 68: 
            case 130: 
            case 146: 
            case 154: {
                int extra = data & 8;
                int withoutFlags = data & 0xFFFFFFF7;
                switch (withoutFlags) {
                    case 2: {
                        return 5 | extra;
                    }
                    case 3: {
                        return 4 | extra;
                    }
                    case 4: {
                        return 2 | extra;
                    }
                    case 5: {
                        return 3 | extra;
                    }
                }
                break;
            }
            case 23: 
            case 158: {
                int dispPower = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 2: {
                        return 5 | dispPower;
                    }
                    case 3: {
                        return 4 | dispPower;
                    }
                    case 4: {
                        return 2 | dispPower;
                    }
                    case 5: {
                        return 3 | dispPower;
                    }
                }
                break;
            }
            case 86: 
            case 91: {
                switch (data) {
                    case 0: {
                        return 1;
                    }
                    case 1: {
                        return 2;
                    }
                    case 2: {
                        return 3;
                    }
                    case 3: {
                        return 0;
                    }
                }
                break;
            }
            case 17: 
            case 162: 
            case 170: {
                if (data < 4 || data > 11) break;
                data ^= 0xC;
                break;
            }
            case 93: 
            case 94: 
            case 149: 
            case 150: {
                int dir = data & 3;
                int delay = data - dir;
                switch (dir) {
                    case 0: {
                        return 1 | delay;
                    }
                    case 1: {
                        return 2 | delay;
                    }
                    case 2: {
                        return 3 | delay;
                    }
                    case 3: {
                        return 0 | delay;
                    }
                }
                break;
            }
            case 96: 
            case 167: {
                int withoutOrientation = data & 0xFFFFFFFC;
                int orientation = data & 3;
                switch (orientation) {
                    case 0: {
                        return 3 | withoutOrientation;
                    }
                    case 1: {
                        return 2 | withoutOrientation;
                    }
                    case 2: {
                        return 0 | withoutOrientation;
                    }
                    case 3: {
                        return 1 | withoutOrientation;
                    }
                }
                break;
            }
            case 29: 
            case 33: 
            case 34: {
                int rest = data & 0xFFFFFFF8;
                switch (data & 7) {
                    case 2: {
                        return 5 | rest;
                    }
                    case 3: {
                        return 4 | rest;
                    }
                    case 4: {
                        return 2 | rest;
                    }
                    case 5: {
                        return 3 | rest;
                    }
                }
                break;
            }
            case 99: 
            case 100: {
                if (data >= 10) {
                    return data;
                }
                return data * 3 % 10;
            }
            case 106: {
                return (data << 1 | data >> 3) & 0xF;
            }
            case 107: {
                return data + 1 & 3 | data & 0xFFFFFFFC;
            }
            case 145: {
                int damage = data & 0xFFFFFFFC;
                switch (data & 3) {
                    case 0: {
                        return 3 | damage;
                    }
                    case 2: {
                        return 1 | damage;
                    }
                    case 1: {
                        return 0 | damage;
                    }
                    case 3: {
                        return 2 | damage;
                    }
                }
                break;
            }
            case 26: {
                return data & 0xFFFFFFFC | data + 1 & 3;
            }
            case 144: {
                switch (data) {
                    case 2: {
                        return 5;
                    }
                    case 3: {
                        return 4;
                    }
                    case 4: {
                        return 2;
                    }
                    case 5: {
                        return 3;
                    }
                }
            }
        }
        return data;
    }

    public static int rotate90Reverse(int type, int data) {
        switch (type) {
            case 50: 
            case 75: 
            case 76: {
                switch (data) {
                    case 3: {
                        return 1;
                    }
                    case 4: {
                        return 2;
                    }
                    case 2: {
                        return 3;
                    }
                    case 1: {
                        return 4;
                    }
                }
                break;
            }
            case 66: {
                switch (data) {
                    case 7: {
                        return 6;
                    }
                    case 8: {
                        return 7;
                    }
                    case 9: {
                        return 8;
                    }
                    case 6: {
                        return 9;
                    }
                }
            }
            case 27: 
            case 28: 
            case 157: {
                int power = data & 0xFFFFFFF8;
                switch (data & 7) {
                    case 1: {
                        return 0 | power;
                    }
                    case 0: {
                        return 1 | power;
                    }
                    case 5: {
                        return 2 | power;
                    }
                    case 4: {
                        return 3 | power;
                    }
                    case 2: {
                        return 4 | power;
                    }
                    case 3: {
                        return 5 | power;
                    }
                }
                break;
            }
            case 53: 
            case 67: 
            case 108: 
            case 109: 
            case 114: 
            case 128: 
            case 134: 
            case 135: 
            case 136: 
            case 156: 
            case 163: 
            case 164: {
                switch (data) {
                    case 2: {
                        return 0;
                    }
                    case 3: {
                        return 1;
                    }
                    case 1: {
                        return 2;
                    }
                    case 0: {
                        return 3;
                    }
                    case 6: {
                        return 4;
                    }
                    case 7: {
                        return 5;
                    }
                    case 5: {
                        return 6;
                    }
                    case 4: {
                        return 7;
                    }
                }
                break;
            }
            case 77: 
            case 143: {
                int thrown = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 3: {
                        return 1 | thrown;
                    }
                    case 4: {
                        return 2 | thrown;
                    }
                    case 2: {
                        return 3 | thrown;
                    }
                    case 1: {
                        return 4 | thrown;
                    }
                }
                break;
            }
            case 69: {
                int thrown = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 3: {
                        return 1 | thrown;
                    }
                    case 4: {
                        return 2 | thrown;
                    }
                    case 2: {
                        return 3 | thrown;
                    }
                    case 1: {
                        return 4 | thrown;
                    }
                    case 6: {
                        return 5 | thrown;
                    }
                    case 5: {
                        return 6 | thrown;
                    }
                    case 0: {
                        return 7 | thrown;
                    }
                    case 7: {
                        return 0 | thrown;
                    }
                }
                break;
            }
            case 64: 
            case 71: {
                if ((data & 8) != 0) break;
            }
            case 127: 
            case 131: {
                int extra = data & 0xFFFFFFFC;
                int withoutFlags = data & 3;
                switch (withoutFlags) {
                    case 1: {
                        return 0 | extra;
                    }
                    case 2: {
                        return 1 | extra;
                    }
                    case 3: {
                        return 2 | extra;
                    }
                    case 0: {
                        return 3 | extra;
                    }
                }
                break;
            }
            case 63: {
                return (data + 12) % 16;
            }
            case 54: 
            case 61: 
            case 62: 
            case 65: 
            case 68: 
            case 130: 
            case 146: 
            case 154: {
                int extra = data & 8;
                int withoutFlags = data & 0xFFFFFFF7;
                switch (withoutFlags) {
                    case 5: {
                        return 2 | extra;
                    }
                    case 4: {
                        return 3 | extra;
                    }
                    case 2: {
                        return 4 | extra;
                    }
                    case 3: {
                        return 5 | extra;
                    }
                }
                break;
            }
            case 23: 
            case 158: {
                int dispPower = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 5: {
                        return 2 | dispPower;
                    }
                    case 4: {
                        return 3 | dispPower;
                    }
                    case 2: {
                        return 4 | dispPower;
                    }
                    case 3: {
                        return 5 | dispPower;
                    }
                }
                break;
            }
            case 86: 
            case 91: {
                switch (data) {
                    case 1: {
                        return 0;
                    }
                    case 2: {
                        return 1;
                    }
                    case 3: {
                        return 2;
                    }
                    case 0: {
                        return 3;
                    }
                }
                break;
            }
            case 17: 
            case 162: 
            case 170: {
                if (data < 4 || data > 11) break;
                data ^= 0xC;
                break;
            }
            case 93: 
            case 94: 
            case 149: 
            case 150: {
                int dir = data & 3;
                int delay = data - dir;
                switch (dir) {
                    case 1: {
                        return 0 | delay;
                    }
                    case 2: {
                        return 1 | delay;
                    }
                    case 3: {
                        return 2 | delay;
                    }
                    case 0: {
                        return 3 | delay;
                    }
                }
                break;
            }
            case 96: 
            case 167: {
                int withoutOrientation = data & 0xFFFFFFFC;
                int orientation = data & 3;
                switch (orientation) {
                    case 3: {
                        return 0 | withoutOrientation;
                    }
                    case 2: {
                        return 1 | withoutOrientation;
                    }
                    case 0: {
                        return 2 | withoutOrientation;
                    }
                    case 1: {
                        return 3 | withoutOrientation;
                    }
                }
            }
            case 29: 
            case 33: 
            case 34: {
                int rest = data & 0xFFFFFFF8;
                switch (data & 7) {
                    case 5: {
                        return 2 | rest;
                    }
                    case 4: {
                        return 3 | rest;
                    }
                    case 2: {
                        return 4 | rest;
                    }
                    case 3: {
                        return 5 | rest;
                    }
                }
                break;
            }
            case 99: 
            case 100: {
                if (data >= 10) {
                    return data;
                }
                return data * 7 % 10;
            }
            case 106: {
                return (data >> 1 | data << 3) & 0xF;
            }
            case 107: {
                return data + 3 & 3 | data & 0xFFFFFFFC;
            }
            case 145: {
                int damage = data & 0xFFFFFFFC;
                switch (data & 3) {
                    case 0: {
                        return 1 | damage;
                    }
                    case 2: {
                        return 3 | damage;
                    }
                    case 1: {
                        return 2 | damage;
                    }
                    case 3: {
                        return 0 | damage;
                    }
                }
                break;
            }
            case 26: {
                return data & 0xFFFFFFFC | data - 1 & 3;
            }
            case 144: {
                switch (data) {
                    case 2: {
                        return 4;
                    }
                    case 3: {
                        return 5;
                    }
                    case 4: {
                        return 3;
                    }
                    case 5: {
                        return 2;
                    }
                }
            }
        }
        return data;
    }

    public static int flip(int type, int data) {
        return BlockData.rotate90(type, BlockData.rotate90(type, data));
    }

    public static int flip(int type, int data, CuboidClipboard.FlipDirection direction) {
        int flipX = 0;
        int flipY = 0;
        int flipZ = 0;
        switch (direction) {
            case NORTH_SOUTH: {
                flipZ = 1;
                break;
            }
            case WEST_EAST: {
                flipX = 1;
                break;
            }
            case UP_DOWN: {
                flipY = 1;
            }
        }
        switch (type) {
            case 50: 
            case 75: 
            case 76: {
                if (data < 1 || data > 4) break;
                switch (data) {
                    case 1: {
                        return data + flipX;
                    }
                    case 2: {
                        return data - flipX;
                    }
                    case 3: {
                        return data + flipZ;
                    }
                    case 4: {
                        return data - flipZ;
                    }
                }
                break;
            }
            case 77: 
            case 143: {
                switch (data & 0xFFFFFFF7) {
                    case 1: {
                        return data + flipX;
                    }
                    case 2: {
                        return data - flipX;
                    }
                    case 3: {
                        return data + flipZ;
                    }
                    case 4: {
                        return data - flipZ;
                    }
                    case 0: 
                    case 5: {
                        return data ^ flipY * 5;
                    }
                }
                break;
            }
            case 69: {
                switch (data & 0xFFFFFFF7) {
                    case 1: {
                        return data + flipX;
                    }
                    case 2: {
                        return data - flipX;
                    }
                    case 3: {
                        return data + flipZ;
                    }
                    case 4: {
                        return data - flipZ;
                    }
                    case 5: 
                    case 7: {
                        return data ^ flipY << 1;
                    }
                    case 0: 
                    case 6: {
                        return data ^ flipY * 6;
                    }
                }
                break;
            }
            case 66: {
                switch (data) {
                    case 6: {
                        return data + flipX + flipZ * 3;
                    }
                    case 7: {
                        return data - flipX + flipZ;
                    }
                    case 8: {
                        return data + flipX - flipZ;
                    }
                    case 9: {
                        return data - flipX - flipZ * 3;
                    }
                }
            }
            case 27: 
            case 28: 
            case 157: {
                switch (data & 7) {
                    case 0: 
                    case 1: {
                        return data;
                    }
                    case 2: 
                    case 3: {
                        return data ^ flipX;
                    }
                    case 4: 
                    case 5: {
                        return data ^ flipZ;
                    }
                }
                break;
            }
            case 44: 
            case 126: {
                return data ^ flipY << 3;
            }
            case 53: 
            case 67: 
            case 108: 
            case 109: 
            case 114: 
            case 128: 
            case 134: 
            case 135: 
            case 136: 
            case 156: 
            case 163: 
            case 164: {
                switch (data ^= flipY << 2) {
                    case 0: 
                    case 1: 
                    case 4: 
                    case 5: {
                        return data ^ flipX;
                    }
                    case 2: 
                    case 3: 
                    case 6: 
                    case 7: {
                        return data ^ flipZ;
                    }
                }
                break;
            }
            case 64: 
            case 71: {
                if ((data & 8) != 0) break;
                switch (data & 3) {
                    case 0: {
                        return data + flipX + flipZ * 3;
                    }
                    case 1: {
                        return data - flipX + flipZ;
                    }
                    case 2: {
                        return data + flipX - flipZ;
                    }
                    case 3: {
                        return data - flipX - flipZ * 3;
                    }
                }
                break;
            }
            case 63: {
                switch (direction) {
                    case NORTH_SOUTH: {
                        return 16 - data & 0xF;
                    }
                    case WEST_EAST: {
                        return 8 - data & 0xF;
                    }
                }
                break;
            }
            case 54: 
            case 61: 
            case 62: 
            case 65: 
            case 68: 
            case 130: 
            case 146: 
            case 154: {
                int extra = data & 8;
                int withoutFlags = data & 0xFFFFFFF7;
                switch (withoutFlags) {
                    case 2: 
                    case 3: {
                        return data ^ flipZ | extra;
                    }
                    case 4: 
                    case 5: {
                        return data ^ flipX | extra;
                    }
                }
                break;
            }
            case 23: 
            case 158: {
                int dispPower = data & 8;
                switch (data & 0xFFFFFFF7) {
                    case 2: 
                    case 3: {
                        return data ^ flipZ | dispPower;
                    }
                    case 4: 
                    case 5: {
                        return data ^ flipX | dispPower;
                    }
                    case 0: 
                    case 1: {
                        return data ^ flipY | dispPower;
                    }
                }
                break;
            }
            case 86: 
            case 91: {
                if (data > 3) break;
            }
            case 93: 
            case 94: 
            case 127: 
            case 131: 
            case 149: 
            case 150: {
                switch (data & 3) {
                    case 0: 
                    case 2: {
                        return data ^ flipZ << 1;
                    }
                    case 1: 
                    case 3: {
                        return data ^ flipX << 1;
                    }
                }
                break;
            }
            case 96: 
            case 167: {
                switch (data & 3) {
                    case 0: 
                    case 1: {
                        return data ^ flipZ;
                    }
                    case 2: 
                    case 3: {
                        return data ^ flipX;
                    }
                }
                break;
            }
            case 29: 
            case 33: 
            case 34: {
                switch (data & 0xFFFFFFF7) {
                    case 0: 
                    case 1: {
                        return data ^ flipY;
                    }
                    case 2: 
                    case 3: {
                        return data ^ flipZ;
                    }
                    case 4: 
                    case 5: {
                        return data ^ flipX;
                    }
                }
                break;
            }
            case 99: 
            case 100: {
                switch (data) {
                    case 1: 
                    case 4: 
                    case 7: {
                        data += flipX * 2;
                        break;
                    }
                    case 3: 
                    case 6: 
                    case 9: {
                        data -= flipX * 2;
                    }
                }
                switch (data) {
                    case 1: 
                    case 2: 
                    case 3: {
                        return data + flipZ * 6;
                    }
                    case 7: 
                    case 8: 
                    case 9: {
                        return data - flipZ * 6;
                    }
                }
                break;
            }
            case 106: {
                int bit2;
                int bit1;
                switch (direction) {
                    case NORTH_SOUTH: {
                        bit1 = 2;
                        bit2 = 8;
                        break;
                    }
                    case WEST_EAST: {
                        bit1 = 1;
                        bit2 = 4;
                        break;
                    }
                    default: {
                        return data;
                    }
                }
                int newData = data & ~(bit1 | bit2);
                if ((data & bit1) != 0) {
                    newData |= bit2;
                }
                if ((data & bit2) != 0) {
                    newData |= bit1;
                }
                return newData;
            }
            case 107: {
                switch (data & 3) {
                    case 0: 
                    case 2: {
                        return data ^ flipZ << 1;
                    }
                    case 1: 
                    case 3: {
                        return data ^ flipX << 1;
                    }
                }
                break;
            }
            case 26: {
                switch (data & 3) {
                    case 0: 
                    case 2: {
                        return data ^ flipZ << 1;
                    }
                    case 1: 
                    case 3: {
                        return data ^ flipX << 1;
                    }
                }
                break;
            }
            case 144: {
                switch (data) {
                    case 2: 
                    case 3: {
                        return data ^ flipZ;
                    }
                    case 4: 
                    case 5: {
                        return data ^ flipX;
                    }
                }
                break;
            }
            case 145: {
                switch (data & 3) {
                    case 0: 
                    case 2: {
                        return data ^ flipZ << 1;
                    }
                    case 1: 
                    case 3: {
                        return data ^ flipX << 1;
                    }
                }
            }
        }
        return data;
    }

    public static int cycle(int type, int data, int increment) {
        if (increment != -1 && increment != 1) {
            throw new IllegalArgumentException("Increment must be 1 or -1.");
        }
        switch (type) {
            case 17: 
            case 162: {
                if (increment == -1) {
                    int store = data & 3;
                    return BlockData.mod((data & 0xFFFFFFFC) + 4, 16) | store;
                }
                int store = data & 0xFFFFFFFC;
                return BlockData.mod((data & 3) + 1, 4) | store;
            }
            case 3: 
            case 24: 
            case 31: {
                if (data > 2) {
                    return -1;
                }
                return BlockData.mod(data + increment, 3);
            }
            case 50: 
            case 75: 
            case 76: {
                if (data < 1 || data > 4) {
                    return -1;
                }
                return BlockData.mod(data - 1 + increment, 4) + 1;
            }
            case 53: 
            case 67: 
            case 108: 
            case 109: 
            case 114: 
            case 128: 
            case 134: 
            case 135: 
            case 136: 
            case 156: 
            case 163: 
            case 164: {
                if (data > 7) {
                    return -1;
                }
                return BlockData.mod(data + increment, 8);
            }
            case 86: 
            case 91: 
            case 98: 
            case 115: 
            case 118: 
            case 125: 
            case 126: 
            case 155: 
            case 170: {
                if (data > 3) {
                    return -1;
                }
                return BlockData.mod(data + increment, 4);
            }
            case 29: 
            case 33: 
            case 43: 
            case 44: 
            case 92: 
            case 97: {
                if (data > 5) {
                    return -1;
                }
                return BlockData.mod(data + increment, 6);
            }
            case 175: {
                int store = data & 8;
                if ((data &= 0xFFFFFFF7) > 5) {
                    return -1;
                }
                return BlockData.mod(data + increment, 6) | store;
            }
            case 59: 
            case 104: 
            case 105: {
                if (data > 6) {
                    return -1;
                }
                return BlockData.mod(data + increment, 7);
            }
            case 38: 
            case 60: {
                if (data > 8) {
                    return -1;
                }
                return BlockData.mod(data + increment, 9);
            }
            case 99: 
            case 100: {
                if (data > 10) {
                    return -1;
                }
                return BlockData.mod(data + increment, 11);
            }
            case 63: 
            case 78: 
            case 81: 
            case 83: 
            case 106: 
            case 127: {
                if (data > 15) {
                    return -1;
                }
                return BlockData.mod(data + increment, 16);
            }
            case 54: 
            case 61: 
            case 62: 
            case 65: 
            case 68: 
            case 130: 
            case 146: 
            case 154: {
                int extra = data & 8;
                int withoutFlags = data & 0xFFFFFFF7;
                if (withoutFlags < 2 || withoutFlags > 5) {
                    return -1;
                }
                return BlockData.mod(withoutFlags - 2 + increment, 4) + 2 | extra;
            }
            case 23: 
            case 158: {
                int store = data & 8;
                if ((data &= 0xFFFFFFF7) > 5) {
                    return -1;
                }
                return BlockData.mod(data + increment, 6) | store;
            }
            case 18: 
            case 93: 
            case 94: 
            case 96: 
            case 107: 
            case 149: 
            case 150: 
            case 161: {
                if (data > 7) {
                    return -1;
                }
                int store = data & 0xFFFFFFFC;
                return BlockData.mod((data & 3) + increment, 4) | store;
            }
            case 66: {
                if (data < 6 || data > 9) {
                    return -1;
                }
                return BlockData.mod(data - 6 + increment, 4) + 6;
            }
            case 6: {
                if ((data & 3) == 3 || data > 15) {
                    return -1;
                }
                int store = data & 0xFFFFFFFC;
                return BlockData.mod((data & 3) + increment, 3) | store;
            }
            case 140: {
                if (data > 13) {
                    return -1;
                }
                return BlockData.mod(data + increment, 14);
            }
            case 35: 
            case 95: 
            case 159: 
            case 160: 
            case 171: {
                if (increment == 1) {
                    data = BlockData.nextClothColor(data);
                } else if (increment == -1) {
                    data = BlockData.prevClothColor(data);
                }
                return data;
            }
        }
        return -1;
    }

    public static int nextClothColor(int data) {
        switch (data) {
            case 0: {
                return 8;
            }
            case 8: {
                return 7;
            }
            case 7: {
                return 15;
            }
            case 15: {
                return 12;
            }
            case 12: {
                return 14;
            }
            case 14: {
                return 1;
            }
            case 1: {
                return 4;
            }
            case 4: {
                return 5;
            }
            case 5: {
                return 13;
            }
            case 13: {
                return 9;
            }
            case 9: {
                return 3;
            }
            case 3: {
                return 11;
            }
            case 11: {
                return 10;
            }
            case 10: {
                return 2;
            }
            case 2: {
                return 6;
            }
            case 6: {
                return 0;
            }
        }
        return 0;
    }

    public static int prevClothColor(int data) {
        switch (data) {
            case 8: {
                return 0;
            }
            case 7: {
                return 8;
            }
            case 15: {
                return 7;
            }
            case 12: {
                return 15;
            }
            case 14: {
                return 12;
            }
            case 1: {
                return 14;
            }
            case 4: {
                return 1;
            }
            case 5: {
                return 4;
            }
            case 13: {
                return 5;
            }
            case 9: {
                return 13;
            }
            case 3: {
                return 9;
            }
            case 11: {
                return 3;
            }
            case 10: {
                return 11;
            }
            case 2: {
                return 10;
            }
            case 6: {
                return 2;
            }
            case 0: {
                return 6;
            }
        }
        return 0;
    }

    private static int mod(int x, int y) {
        int res = x % y;
        return res < 0 ? res + y : res;
    }
}

