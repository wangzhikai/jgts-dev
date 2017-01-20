package net.heteroclinic.computergraphics.derivedfromopengl;
/**
 * Copyright 2016 Zhikai Wang <www.heteroclinic.net>. All rights reserved.
 * Science and Technology Promotion License 
 * All third-party licenses are automatically cascaded.
 * Non-commercial usage of this file is not limited.
 * Commercial usage is allowed, given this file is not modified. 
 * Inheritance/overriding/re-factoring is suggested at higher level.
 * This is a good-will software. Users' liability always. 
 */

/**
 * The class {@code GLColor4ub} is a tuple of (r,g,b,a) -- red, 
 * green, blue and alpha value, range in [0,255] in byte data type.
 * @author      Zhikai Wang &lt;www.heteroclinic.net&gt;
 * @version     1.0
 * @since       1.0
 */
public class GLColor4ub {
	
	public String toString() {
		return "[" + unsignedToBytes(getR()) + "," + unsignedToBytes(getG()) + "," + unsignedToBytes(getB()) + ","
				+ unsignedToBytes(getAlpha()) + "]";
	}

	/**
	 * Convert byte to Java int for String operations or print
	 * <p>
	 * For Java println, byte is signed.
	 * to print in range [0,255], we have to widen the type to show the value as unsigned.
	 * Namely widening byte to int to show the literal byte in range [0,255].
	 * @param b a signed byte type
	 * @return int widen to int to print
	 */
	public static int unsignedToBytes(byte b) {
		return b & 0xFF;
	}

	protected byte[] data = new byte[4];

	public byte getAlpha() {
		return data[3];
	}

	public void setAlpha(byte alpha) {
		data[3] = alpha;
	}

	public GLColor4ub(byte[] data) {
		this.data = data;

	}

	/**
	 * Constructor of GLColor4ub
	 * <p>
	 * @param r red component of a given color
	 * @param g green 
	 * @param b blue
	 * @param alpha the component indicating the opacity
	 */
	public GLColor4ub(byte r, byte g, byte b, byte alpha) {
		setR(r);
		setG(g);
		setB(b);
		setAlpha(alpha);
	}

	public GLColor4ub(byte r, byte g, byte b) {
		this(r, g, b, (byte) 255);
	}

	public void setR(byte r) {
		data[0] = r;
		// the following is wrong
		// if (r > 255)
		// data[0] = (byte) 255;
		// if (r <0 )
		// data[0] = 0;
	}

	public byte getR() {
		return data[0];
	}

	public void setG(byte g) {
		data[1] = g;
		// if (g > 255)
		// data[1] = (byte) 255;
		// if (g <0 )
		// data[1] = 0;
	}

	public byte getG() {
		return data[1];
	}

	public void setRGBA(byte r, byte g, byte b, byte alpha) {
		setR(r);
		setG(g);
		setB(b);
		setAlpha(alpha);
	}

	public void setB(byte b) {
		data[2] = b;
		// if (b > 255)
		// data[2] = (byte) 255;
		// if (b <0 )
		// data[2] = 0;
	}

	public byte getB() {
		return data[2];
	}

	byte[] getColor3i() {
		return data;
	}

	public static void main(String[] args) {
		
		byte j = 0;
		for (int i = 0; i < 257; i++) {
			System.out.println(j);
			System.out.println(unsignedToBytes(j++));
		}
	}
}
/* main() output:
0
0
1
1
2
2
3
3
4
4
5
5
6
6
7
7
8
8
9
9
10
10
11
11
12
12
13
13
14
14
15
15
16
16
17
17
18
18
19
19
20
20
21
21
22
22
23
23
24
24
25
25
26
26
27
27
28
28
29
29
30
30
31
31
32
32
33
33
34
34
35
35
36
36
37
37
38
38
39
39
40
40
41
41
42
42
43
43
44
44
45
45
46
46
47
47
48
48
49
49
50
50
51
51
52
52
53
53
54
54
55
55
56
56
57
57
58
58
59
59
60
60
61
61
62
62
63
63
64
64
65
65
66
66
67
67
68
68
69
69
70
70
71
71
72
72
73
73
74
74
75
75
76
76
77
77
78
78
79
79
80
80
81
81
82
82
83
83
84
84
85
85
86
86
87
87
88
88
89
89
90
90
91
91
92
92
93
93
94
94
95
95
96
96
97
97
98
98
99
99
100
100
101
101
102
102
103
103
104
104
105
105
106
106
107
107
108
108
109
109
110
110
111
111
112
112
113
113
114
114
115
115
116
116
117
117
118
118
119
119
120
120
121
121
122
122
123
123
124
124
125
125
126
126
127
127
-128
128
-127
129
-126
130
-125
131
-124
132
-123
133
-122
134
-121
135
-120
136
-119
137
-118
138
-117
139
-116
140
-115
141
-114
142
-113
143
-112
144
-111
145
-110
146
-109
147
-108
148
-107
149
-106
150
-105
151
-104
152
-103
153
-102
154
-101
155
-100
156
-99
157
-98
158
-97
159
-96
160
-95
161
-94
162
-93
163
-92
164
-91
165
-90
166
-89
167
-88
168
-87
169
-86
170
-85
171
-84
172
-83
173
-82
174
-81
175
-80
176
-79
177
-78
178
-77
179
-76
180
-75
181
-74
182
-73
183
-72
184
-71
185
-70
186
-69
187
-68
188
-67
189
-66
190
-65
191
-64
192
-63
193
-62
194
-61
195
-60
196
-59
197
-58
198
-57
199
-56
200
-55
201
-54
202
-53
203
-52
204
-51
205
-50
206
-49
207
-48
208
-47
209
-46
210
-45
211
-44
212
-43
213
-42
214
-41
215
-40
216
-39
217
-38
218
-37
219
-36
220
-35
221
-34
222
-33
223
-32
224
-31
225
-30
226
-29
227
-28
228
-27
229
-26
230
-25
231
-24
232
-23
233
-22
234
-21
235
-20
236
-19
237
-18
238
-17
239
-16
240
-15
241
-14
242
-13
243
-12
244
-11
245
-10
246
-9
247
-8
248
-7
249
-6
250
-5
251
-4
252
-3
253
-2
254
-1
255
0
0
*/