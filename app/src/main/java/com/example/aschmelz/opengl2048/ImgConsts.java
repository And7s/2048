package com.example.aschmelz.opengl2048;

/**
 * Created by aschmelz on 23/03/2018.
 */


/**
 * Created by aschmelz on 04/03/2018.
 */

public class ImgConsts {
    public static final float[]
        COLOR_BACKGROUND = {250 / 256f, 248 / 256f, 239 / 256f , 1f},
        COLOR_FIELD = {187 / 256f, 173 / 256f, 160 / 256f, 1f},
        COLOR_TEXT_DARK = {119 / 256f, 110 / 256f, 101 / 256f, 1},
        COLOR_TEXT_LIGHT = {238 / 256f, 228 / 256f, 218 / 256f, 1},
        COLOR_BOARD = {187 / 256f, 173 / 256f, 160 / 256f, 1},
        COLOR_TILE_BG = {205 / 256f, 193 / 256f, 180 / 256f, 1},
        COLOR_WHITE = {1, 1, 1, 1},
        COLOR_BLACK = {0, 0, 0, 1};

    public static final int
            SPR_SQUARE = 0,
            FONT_MARRYTODD_37 = 1,
            FONT_MARRYTODD_87 = 2,
            FONT_MARRYTODD_109 = 3,
            FONT_MARRYTODD_77 = 4,
            FONT_MARRYTODD_64 = 5,
            FONT_MARRYTODD_119 = 6,
            FONT_MARRYTODD_38 = 7,
            FONT_MARRYTODD_35 = 8,
            FONT_MARRYTODD_95 = 9,
            FONT_MARRYTODD_94 = 10,
            FONT_MARRYTODD_43 = 11,
            FONT_MARRYTODD_126 = 12,
            FONT_MARRYTODD_61 = 13,
            FONT_MARRYTODD_72 = 14,
            FONT_MARRYTODD_78 = 15,
            FONT_MARRYTODD_85 = 16,
            FONT_MARRYTODD_68 = 17,
            FONT_MARRYTODD_79 = 18,
            FONT_MARRYTODD_81 = 19,
            FONT_MARRYTODD_71 = 20,
            FONT_MARRYTODD_65 = 21,
            FONT_MARRYTODD_60 = 22,
            FONT_MARRYTODD_62 = 23,
            FONT_MARRYTODD_86 = 24,
            FONT_MARRYTODD_66 = 25,
            FONT_MARRYTODD_82 = 26,
            FONT_MARRYTODD_104 = 27,
            FONT_MARRYTODD_110 = 28,
            FONT_MARRYTODD_75 = 29,
            FONT_MARRYTODD_112 = 30,
            FONT_MARRYTODD_113 = 31,
            FONT_MARRYTODD_89 = 32,
            FONT_MARRYTODD_88 = 33,
            FONT_MARRYTODD_100 = 34,
            FONT_MARRYTODD_117 = 35,
            FONT_MARRYTODD_98 = 36,
            FONT_MARRYTODD_103 = 37,
            FONT_MARRYTODD_80 = 38,
            FONT_MARRYTODD_48 = 39,
            FONT_MARRYTODD_56 = 40,
            FONT_MARRYTODD_111 = 41,
            FONT_MARRYTODD_52 = 42,
            FONT_MARRYTODD_54 = 43,
            FONT_MARRYTODD_57 = 44,
            FONT_MARRYTODD_50 = 45,
            FONT_MARRYTODD_67 = 46,
            FONT_MARRYTODD_101 = 47,
            FONT_MARRYTODD_51 = 48,
            FONT_MARRYTODD_83 = 49,
            FONT_MARRYTODD_97 = 50,
            FONT_MARRYTODD_36 = 51,
            FONT_MARRYTODD_53 = 52,
            FONT_MARRYTODD_90 = 53,
            FONT_MARRYTODD_42 = 54,
            FONT_MARRYTODD_69 = 55,
            FONT_MARRYTODD_84 = 56,
            FONT_MARRYTODD_107 = 57,
            FONT_MARRYTODD_70 = 58,
            FONT_MARRYTODD_118 = 59,
            FONT_MARRYTODD_55 = 60,
            FONT_MARRYTODD_121 = 61,
            FONT_MARRYTODD_76 = 62,
            FONT_MARRYTODD_120 = 63,
            FONT_MARRYTODD_49 = 64,
            FONT_MARRYTODD_63 = 65,
            FONT_MARRYTODD_47 = 66,
            FONT_MARRYTODD_122 = 67,
            FONT_MARRYTODD_99 = 68,
            FONT_MARRYTODD_92 = 69,
            FONT_MARRYTODD_115 = 70,
            FONT_MARRYTODD_125 = 71,
            FONT_MARRYTODD_33 = 72,
            FONT_MARRYTODD_45 = 73,
            FONT_MARRYTODD_123 = 74,
            FONT_MARRYTODD_74 = 75,
            FONT_MARRYTODD_116 = 76,
            FONT_MARRYTODD_39 = 77,
            FONT_MARRYTODD_114 = 78,
            FONT_MARRYTODD_34 = 79,
            FONT_MARRYTODD_40 = 80,
            FONT_MARRYTODD_41 = 81,
            FONT_MARRYTODD_102 = 82,
            FONT_MARRYTODD_73 = 83,
            FONT_MARRYTODD_105 = 84,
            FONT_MARRYTODD_108 = 85,
            FONT_MARRYTODD_91 = 86,
            FONT_MARRYTODD_93 = 87,
            FONT_MARRYTODD_106 = 88,
            FONT_MARRYTODD_96 = 89,
            FONT_MARRYTODD_44 = 90,
            FONT_MARRYTODD_59 = 91,
            FONT_MARRYTODD_58 = 92,
            FONT_MARRYTODD_46 = 93,
            FONT_MARRYTODD_124 = 94,
            SPR_SOLID_WHITE = 95,
            SPR_SQUARE_LT = 96,
            SPR_SQUARE_LB = 97,
            SPR_SQUARE_RT = 98,
            SPR_SQUARE_RB = 99,
            SPR_SQUARE_H = 100,
            SPR_SQUARE_V = 101;
private static final int rad = 40;
    public static final int[][] dimensions = new int[][]

            {
                { 0, 0, 256, 256},     // square
                { 260, 0, 92, 126},     // font_marrytodd_37
                { 356, 0, 90, 126},     // font_marrytodd_87
                { 450, 0, 89, 126},     // font_marrytodd_109
                { 543, 0, 89, 126},     // font_marrytodd_77
                { 636, 0, 88, 126},     // font_marrytodd_64
                { 728, 0, 82, 126},     // font_marrytodd_119
                { 814, 0, 80, 126},     // font_marrytodd_38
                { 898, 0, 80, 126},     // font_marrytodd_35
                { 260, 130, 78, 126},     // font_marrytodd_95
                { 342, 130, 76, 126},     // font_marrytodd_94
                { 422, 130, 76, 126},     // font_marrytodd_43
                { 502, 130, 74, 126},     // font_marrytodd_126
                { 580, 130, 71, 126},     // font_marrytodd_61
                { 655, 130, 70, 126},     // font_marrytodd_72
                { 729, 130, 70, 126},     // font_marrytodd_78
                { 803, 130, 69, 126},     // font_marrytodd_85
                { 876, 130, 68, 126},     // font_marrytodd_68
                { 948, 130, 68, 126},     // font_marrytodd_79
                { 0, 260, 68, 126},     // font_marrytodd_81
                { 72, 260, 67, 126},     // font_marrytodd_71
                { 143, 260, 67, 126},     // font_marrytodd_65
                { 214, 260, 66, 126},     // font_marrytodd_60
                { 284, 260, 66, 126},     // font_marrytodd_62
                { 354, 260, 65, 126},     // font_marrytodd_86
                { 423, 260, 64, 126},     // font_marrytodd_66
                { 491, 260, 64, 126},     // font_marrytodd_82
                { 559, 260, 63, 126},     // font_marrytodd_104
                { 626, 260, 63, 126},     // font_marrytodd_110
                { 693, 260, 63, 126},     // font_marrytodd_75
                { 760, 260, 62, 126},     // font_marrytodd_112
                { 826, 260, 62, 126},     // font_marrytodd_113
                { 892, 260, 62, 126},     // font_marrytodd_89
                { 958, 260, 62, 126},     // font_marrytodd_88
                { 0, 390, 62, 126},     // font_marrytodd_100
                { 66, 390, 62, 126},     // font_marrytodd_117
                { 132, 390, 62, 126},     // font_marrytodd_98
                { 198, 390, 62, 126},     // font_marrytodd_103
                { 264, 390, 62, 126},     // font_marrytodd_80
                { 330, 390, 62, 126},     // font_marrytodd_48
                { 396, 390, 61, 126},     // font_marrytodd_56
                { 461, 390, 60, 126},     // font_marrytodd_111
                { 525, 390, 60, 126},     // font_marrytodd_52
                { 589, 390, 60, 126},     // font_marrytodd_54
                { 653, 390, 60, 126},     // font_marrytodd_57
                { 717, 390, 59, 126},     // font_marrytodd_50
                { 780, 390, 59, 126},     // font_marrytodd_67
                { 843, 390, 58, 126},     // font_marrytodd_101
                { 905, 390, 58, 126},     // font_marrytodd_51
                { 0, 520, 58, 126},     // font_marrytodd_83
                { 62, 520, 58, 126},     // font_marrytodd_97
                { 124, 520, 58, 126},     // font_marrytodd_36
                { 186, 520, 57, 126},     // font_marrytodd_53
                { 247, 520, 57, 126},     // font_marrytodd_90
                { 308, 520, 57, 126},     // font_marrytodd_42
                { 369, 520, 57, 126},     // font_marrytodd_69
                { 430, 520, 57, 126},     // font_marrytodd_84
                { 491, 520, 57, 126},     // font_marrytodd_107
                { 552, 520, 56, 126},     // font_marrytodd_70
                { 612, 520, 56, 126},     // font_marrytodd_118
                { 672, 520, 56, 126},     // font_marrytodd_55
                { 732, 520, 56, 126},     // font_marrytodd_121
                { 792, 520, 56, 126},     // font_marrytodd_76
                { 852, 520, 55, 126},     // font_marrytodd_120
                { 911, 520, 54, 126},     // font_marrytodd_49
                { 967, 390, 53, 126},     // font_marrytodd_63
                { 0, 650, 52, 126},     // font_marrytodd_47
                { 56, 650, 52, 126},     // font_marrytodd_122
                { 969, 520, 51, 126},     // font_marrytodd_99
                { 112, 650, 50, 126},     // font_marrytodd_92
                { 166, 650, 49, 126},     // font_marrytodd_115
                { 219, 650, 48, 126},     // font_marrytodd_125
                { 271, 650, 48, 126},     // font_marrytodd_33
                { 323, 650, 48, 126},     // font_marrytodd_45
                { 375, 650, 48, 126},     // font_marrytodd_123
                { 427, 650, 46, 126},     // font_marrytodd_74
                { 477, 650, 45, 126},     // font_marrytodd_116
                { 526, 650, 44, 126},     // font_marrytodd_39
                { 574, 650, 44, 126},     // font_marrytodd_114
                { 622, 650, 44, 126},     // font_marrytodd_34
                { 670, 650, 43, 126},     // font_marrytodd_40
                { 717, 650, 43, 126},     // font_marrytodd_41
                { 764, 650, 42, 126},     // font_marrytodd_102
                { 810, 650, 39, 126},     // font_marrytodd_73
                { 982, 0, 37, 126},     // font_marrytodd_105
                { 853, 650, 37, 126},     // font_marrytodd_108
                { 894, 650, 37, 126},     // font_marrytodd_91
                { 935, 650, 37, 126},     // font_marrytodd_93
                { 976, 650, 37, 126},     // font_marrytodd_106
                { 0, 780, 35, 126},     // font_marrytodd_96
                { 39, 780, 34, 126},     // font_marrytodd_44
                { 77, 780, 34, 126},     // font_marrytodd_59
                { 115, 780, 34, 126},     // font_marrytodd_58
                { 153, 780, 34, 126},     // font_marrytodd_46
                { 191, 780, 31, 126},     // font_marrytodd_124
                { 50, 50, 2, 2},        // solid white
                    {0, 0, rad, rad}, //lt
                    {0, 256 - rad, rad, rad}, // lb
                    {256 - rad, 0, rad, rad}, //rt
                    {256 - rad, 256 - rad, rad, rad}, //rb

                    {0, rad, 256, 256 - rad * 2}, // vertical
                    {rad, 0, 256 - rad * 2, 256}, // horizontal

            };
}

