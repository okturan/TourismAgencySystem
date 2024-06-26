PGDMP  0                    |            tourism_agency    16.3 (Postgres.app)    16.3 Y    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16852    tourism_agency    DATABASE     z   CREATE DATABASE tourism_agency WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.UTF-8';
    DROP DATABASE tourism_agency;
                postgres    false            �            1259    16989    board_types    TABLE     f   CREATE TABLE public.board_types (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
    DROP TABLE public.board_types;
       public         heap    postgres    false            �            1259    16988    board_types_id_seq    SEQUENCE     �   CREATE SEQUENCE public.board_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.board_types_id_seq;
       public          postgres    false    227            �           0    0    board_types_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.board_types_id_seq OWNED BY public.board_types.id;
          public          postgres    false    226            �            1259    16932    guests    TABLE     �   CREATE TABLE public.guests (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    phone character varying(20),
    email character varying(255),
    identification character varying(50)
);
    DROP TABLE public.guests;
       public         heap    postgres    false            �            1259    16931    guests_id_seq    SEQUENCE     �   CREATE SEQUENCE public.guests_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.guests_id_seq;
       public          postgres    false    220            �           0    0    guests_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.guests_id_seq OWNED BY public.guests.id;
          public          postgres    false    219            �            1259    17013    hotel_amenities    TABLE     j   CREATE TABLE public.hotel_amenities (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
 #   DROP TABLE public.hotel_amenities;
       public         heap    postgres    false            �            1259    17012    hotel_amenities_id_seq    SEQUENCE     �   CREATE SEQUENCE public.hotel_amenities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.hotel_amenities_id_seq;
       public          postgres    false    230            �           0    0    hotel_amenities_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.hotel_amenities_id_seq OWNED BY public.hotel_amenities.id;
          public          postgres    false    229            �            1259    16905    hotels    TABLE     �  CREATE TABLE public.hotels (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    phone character varying(20),
    email character varying(255),
    stars integer,
    address_line text,
    country character varying(100),
    city character varying(100),
    district character varying(100),
    CONSTRAINT hotels_stars_check CHECK (((stars >= 1) AND (stars <= 5)))
);
    DROP TABLE public.hotels;
       public         heap    postgres    false            �            1259    16997    hotels_board_types    TABLE     n   CREATE TABLE public.hotels_board_types (
    hotel_id integer NOT NULL,
    board_type_id integer NOT NULL
);
 &   DROP TABLE public.hotels_board_types;
       public         heap    postgres    false            �            1259    17021    hotels_hotel_amenities    TABLE     u   CREATE TABLE public.hotels_hotel_amenities (
    hotel_id integer NOT NULL,
    hotel_amenity_id integer NOT NULL
);
 *   DROP TABLE public.hotels_hotel_amenities;
       public         heap    postgres    false            �            1259    16904    hotels_id_seq    SEQUENCE     �   CREATE SEQUENCE public.hotels_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.hotels_id_seq;
       public          postgres    false    216            �           0    0    hotels_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.hotels_id_seq OWNED BY public.hotels.id;
          public          postgres    false    215            �            1259    16973    hotels_seasons    TABLE     f   CREATE TABLE public.hotels_seasons (
    hotel_id integer NOT NULL,
    season_id integer NOT NULL
);
 "   DROP TABLE public.hotels_seasons;
       public         heap    postgres    false            �            1259    16945    reservations    TABLE     l  CREATE TABLE public.reservations (
    id integer NOT NULL,
    room_id integer NOT NULL,
    primary_guest_id integer NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    calculated_cost_usd numeric(7,2) NOT NULL,
    num_adults integer NOT NULL,
    num_children integer NOT NULL,
    CONSTRAINT reservations_calculated_cost_usd_check CHECK ((calculated_cost_usd >= (0)::numeric)),
    CONSTRAINT reservations_check CHECK ((end_date > start_date)),
    CONSTRAINT reservations_num_adults_check CHECK ((num_adults >= 1)),
    CONSTRAINT reservations_num_children_check CHECK ((num_children >= 0))
);
     DROP TABLE public.reservations;
       public         heap    postgres    false            �            1259    16944    reservations_id_seq    SEQUENCE     �   CREATE SEQUENCE public.reservations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.reservations_id_seq;
       public          postgres    false    222            �           0    0    reservations_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.reservations_id_seq OWNED BY public.reservations.id;
          public          postgres    false    221            �            1259    17037    room_amenities    TABLE     i   CREATE TABLE public.room_amenities (
    id integer NOT NULL,
    name character varying(50) NOT NULL
);
 "   DROP TABLE public.room_amenities;
       public         heap    postgres    false            �            1259    17036    room_amenities_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_amenities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 ,   DROP SEQUENCE public.room_amenities_id_seq;
       public          postgres    false    233            �           0    0    room_amenities_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.room_amenities_id_seq OWNED BY public.room_amenities.id;
          public          postgres    false    232            �            1259    16915    rooms    TABLE     b  CREATE TABLE public.rooms (
    id integer NOT NULL,
    hotel_id integer NOT NULL,
    name character varying(255) NOT NULL,
    capacity integer NOT NULL,
    size_sqm integer NOT NULL,
    stock integer NOT NULL,
    room_type character varying(50),
    adult_price_usd integer NOT NULL,
    child_price_usd integer NOT NULL,
    CONSTRAINT rooms_adult_price_usd_check CHECK (((adult_price_usd)::numeric >= (0)::numeric)),
    CONSTRAINT rooms_child_price_usd_check CHECK (((child_price_usd)::numeric >= (0)::numeric)),
    CONSTRAINT rooms_size_sqm_check CHECK (((size_sqm >= 0) AND (size_sqm <= 999)))
);
    DROP TABLE public.rooms;
       public         heap    postgres    false            �            1259    16914    rooms_id_seq    SEQUENCE     �   CREATE SEQUENCE public.rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.rooms_id_seq;
       public          postgres    false    218            �           0    0    rooms_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.rooms_id_seq OWNED BY public.rooms.id;
          public          postgres    false    217            �            1259    17045    rooms_room_amenities    TABLE     q   CREATE TABLE public.rooms_room_amenities (
    room_id integer NOT NULL,
    room_amenity_id integer NOT NULL
);
 (   DROP TABLE public.rooms_room_amenities;
       public         heap    postgres    false            �            1259    16966    seasons    TABLE     �   CREATE TABLE public.seasons (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    CONSTRAINT seasons_check CHECK ((end_date > start_date))
);
    DROP TABLE public.seasons;
       public         heap    postgres    false            �            1259    16965    seasons_id_seq    SEQUENCE     �   CREATE SEQUENCE public.seasons_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.seasons_id_seq;
       public          postgres    false    224            �           0    0    seasons_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.seasons_id_seq OWNED BY public.seasons.id;
          public          postgres    false    223            �           2604    16992    board_types id    DEFAULT     p   ALTER TABLE ONLY public.board_types ALTER COLUMN id SET DEFAULT nextval('public.board_types_id_seq'::regclass);
 =   ALTER TABLE public.board_types ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    226    227    227            �           2604    16935 	   guests id    DEFAULT     f   ALTER TABLE ONLY public.guests ALTER COLUMN id SET DEFAULT nextval('public.guests_id_seq'::regclass);
 8   ALTER TABLE public.guests ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    220    219    220            �           2604    17016    hotel_amenities id    DEFAULT     x   ALTER TABLE ONLY public.hotel_amenities ALTER COLUMN id SET DEFAULT nextval('public.hotel_amenities_id_seq'::regclass);
 A   ALTER TABLE public.hotel_amenities ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    230    229    230            �           2604    16908 	   hotels id    DEFAULT     f   ALTER TABLE ONLY public.hotels ALTER COLUMN id SET DEFAULT nextval('public.hotels_id_seq'::regclass);
 8   ALTER TABLE public.hotels ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    215    216    216            �           2604    16948    reservations id    DEFAULT     r   ALTER TABLE ONLY public.reservations ALTER COLUMN id SET DEFAULT nextval('public.reservations_id_seq'::regclass);
 >   ALTER TABLE public.reservations ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    221    222    222            �           2604    17040    room_amenities id    DEFAULT     v   ALTER TABLE ONLY public.room_amenities ALTER COLUMN id SET DEFAULT nextval('public.room_amenities_id_seq'::regclass);
 @   ALTER TABLE public.room_amenities ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    232    233    233            �           2604    16918    rooms id    DEFAULT     d   ALTER TABLE ONLY public.rooms ALTER COLUMN id SET DEFAULT nextval('public.rooms_id_seq'::regclass);
 7   ALTER TABLE public.rooms ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    218    217    218            �           2604    16969 
   seasons id    DEFAULT     h   ALTER TABLE ONLY public.seasons ALTER COLUMN id SET DEFAULT nextval('public.seasons_id_seq'::regclass);
 9   ALTER TABLE public.seasons ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    224    224            �          0    16989    board_types 
   TABLE DATA           /   COPY public.board_types (id, name) FROM stdin;
    public          postgres    false    227   �m       �          0    16932    guests 
   TABLE DATA           H   COPY public.guests (id, name, phone, email, identification) FROM stdin;
    public          postgres    false    220   n       �          0    17013    hotel_amenities 
   TABLE DATA           3   COPY public.hotel_amenities (id, name) FROM stdin;
    public          postgres    false    230   o       �          0    16905    hotels 
   TABLE DATA           f   COPY public.hotels (id, name, phone, email, stars, address_line, country, city, district) FROM stdin;
    public          postgres    false    216   �o       �          0    16997    hotels_board_types 
   TABLE DATA           E   COPY public.hotels_board_types (hotel_id, board_type_id) FROM stdin;
    public          postgres    false    228   q       �          0    17021    hotels_hotel_amenities 
   TABLE DATA           L   COPY public.hotels_hotel_amenities (hotel_id, hotel_amenity_id) FROM stdin;
    public          postgres    false    231   Tq       �          0    16973    hotels_seasons 
   TABLE DATA           =   COPY public.hotels_seasons (hotel_id, season_id) FROM stdin;
    public          postgres    false    225   �q       �          0    16945    reservations 
   TABLE DATA           �   COPY public.reservations (id, room_id, primary_guest_id, start_date, end_date, calculated_cost_usd, num_adults, num_children) FROM stdin;
    public          postgres    false    222   �q       �          0    17037    room_amenities 
   TABLE DATA           2   COPY public.room_amenities (id, name) FROM stdin;
    public          postgres    false    233   Wr       �          0    16915    rooms 
   TABLE DATA           {   COPY public.rooms (id, hotel_id, name, capacity, size_sqm, stock, room_type, adult_price_usd, child_price_usd) FROM stdin;
    public          postgres    false    218   �r       �          0    17045    rooms_room_amenities 
   TABLE DATA           H   COPY public.rooms_room_amenities (room_id, room_amenity_id) FROM stdin;
    public          postgres    false    234   �s       �          0    16966    seasons 
   TABLE DATA           A   COPY public.seasons (id, name, start_date, end_date) FROM stdin;
    public          postgres    false    224   Ot       �           0    0    board_types_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.board_types_id_seq', 8, true);
          public          postgres    false    226            �           0    0    guests_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.guests_id_seq', 6, true);
          public          postgres    false    219            �           0    0    hotel_amenities_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.hotel_amenities_id_seq', 8, true);
          public          postgres    false    229            �           0    0    hotels_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.hotels_id_seq', 10, true);
          public          postgres    false    215            �           0    0    reservations_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.reservations_id_seq', 6, true);
          public          postgres    false    221            �           0    0    room_amenities_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.room_amenities_id_seq', 6, true);
          public          postgres    false    232            �           0    0    rooms_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.rooms_id_seq', 17, true);
          public          postgres    false    217            �           0    0    seasons_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.seasons_id_seq', 5, true);
          public          postgres    false    223            �           2606    16996     board_types board_types_name_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.board_types
    ADD CONSTRAINT board_types_name_key UNIQUE (name);
 J   ALTER TABLE ONLY public.board_types DROP CONSTRAINT board_types_name_key;
       public            postgres    false    227            �           2606    16994    board_types board_types_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.board_types
    ADD CONSTRAINT board_types_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.board_types DROP CONSTRAINT board_types_pkey;
       public            postgres    false    227            �           2606    16943    guests guests_email_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.guests
    ADD CONSTRAINT guests_email_key UNIQUE (email);
 A   ALTER TABLE ONLY public.guests DROP CONSTRAINT guests_email_key;
       public            postgres    false    220            �           2606    16941    guests guests_phone_key 
   CONSTRAINT     S   ALTER TABLE ONLY public.guests
    ADD CONSTRAINT guests_phone_key UNIQUE (phone);
 A   ALTER TABLE ONLY public.guests DROP CONSTRAINT guests_phone_key;
       public            postgres    false    220            �           2606    16939    guests guests_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.guests
    ADD CONSTRAINT guests_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.guests DROP CONSTRAINT guests_pkey;
       public            postgres    false    220            �           2606    17020 (   hotel_amenities hotel_amenities_name_key 
   CONSTRAINT     c   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT hotel_amenities_name_key UNIQUE (name);
 R   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT hotel_amenities_name_key;
       public            postgres    false    230            �           2606    17018 $   hotel_amenities hotel_amenities_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT hotel_amenities_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT hotel_amenities_pkey;
       public            postgres    false    230            �           2606    17001 *   hotels_board_types hotels_board_types_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.hotels_board_types
    ADD CONSTRAINT hotels_board_types_pkey PRIMARY KEY (hotel_id, board_type_id);
 T   ALTER TABLE ONLY public.hotels_board_types DROP CONSTRAINT hotels_board_types_pkey;
       public            postgres    false    228    228            �           2606    17025 2   hotels_hotel_amenities hotels_hotel_amenities_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.hotels_hotel_amenities
    ADD CONSTRAINT hotels_hotel_amenities_pkey PRIMARY KEY (hotel_id, hotel_amenity_id);
 \   ALTER TABLE ONLY public.hotels_hotel_amenities DROP CONSTRAINT hotels_hotel_amenities_pkey;
       public            postgres    false    231    231            �           2606    16913    hotels hotels_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.hotels
    ADD CONSTRAINT hotels_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.hotels DROP CONSTRAINT hotels_pkey;
       public            postgres    false    216            �           2606    16977 "   hotels_seasons hotels_seasons_pkey 
   CONSTRAINT     q   ALTER TABLE ONLY public.hotels_seasons
    ADD CONSTRAINT hotels_seasons_pkey PRIMARY KEY (hotel_id, season_id);
 L   ALTER TABLE ONLY public.hotels_seasons DROP CONSTRAINT hotels_seasons_pkey;
       public            postgres    false    225    225            �           2606    16954    reservations reservations_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_pkey;
       public            postgres    false    222            �           2606    17044 &   room_amenities room_amenities_name_key 
   CONSTRAINT     a   ALTER TABLE ONLY public.room_amenities
    ADD CONSTRAINT room_amenities_name_key UNIQUE (name);
 P   ALTER TABLE ONLY public.room_amenities DROP CONSTRAINT room_amenities_name_key;
       public            postgres    false    233            �           2606    17042 "   room_amenities room_amenities_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.room_amenities
    ADD CONSTRAINT room_amenities_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.room_amenities DROP CONSTRAINT room_amenities_pkey;
       public            postgres    false    233            �           2606    16925    rooms rooms_name_hotel_id_key 
   CONSTRAINT     b   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_name_hotel_id_key UNIQUE (name, hotel_id);
 G   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_name_hotel_id_key;
       public            postgres    false    218    218            �           2606    16923    rooms rooms_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_pkey;
       public            postgres    false    218            �           2606    17049 .   rooms_room_amenities rooms_room_amenities_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.rooms_room_amenities
    ADD CONSTRAINT rooms_room_amenities_pkey PRIMARY KEY (room_id, room_amenity_id);
 X   ALTER TABLE ONLY public.rooms_room_amenities DROP CONSTRAINT rooms_room_amenities_pkey;
       public            postgres    false    234    234            �           2606    16972    seasons seasons_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.seasons
    ADD CONSTRAINT seasons_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.seasons DROP CONSTRAINT seasons_pkey;
       public            postgres    false    224            �           2606    17007 8   hotels_board_types hotels_board_types_board_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_board_types
    ADD CONSTRAINT hotels_board_types_board_type_id_fkey FOREIGN KEY (board_type_id) REFERENCES public.board_types(id);
 b   ALTER TABLE ONLY public.hotels_board_types DROP CONSTRAINT hotels_board_types_board_type_id_fkey;
       public          postgres    false    227    3554    228            �           2606    17002 3   hotels_board_types hotels_board_types_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_board_types
    ADD CONSTRAINT hotels_board_types_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 ]   ALTER TABLE ONLY public.hotels_board_types DROP CONSTRAINT hotels_board_types_hotel_id_fkey;
       public          postgres    false    3534    228    216            �           2606    17031 C   hotels_hotel_amenities hotels_hotel_amenities_hotel_amenity_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_hotel_amenities
    ADD CONSTRAINT hotels_hotel_amenities_hotel_amenity_id_fkey FOREIGN KEY (hotel_amenity_id) REFERENCES public.hotel_amenities(id);
 m   ALTER TABLE ONLY public.hotels_hotel_amenities DROP CONSTRAINT hotels_hotel_amenities_hotel_amenity_id_fkey;
       public          postgres    false    230    231    3560            �           2606    17026 ;   hotels_hotel_amenities hotels_hotel_amenities_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_hotel_amenities
    ADD CONSTRAINT hotels_hotel_amenities_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 e   ALTER TABLE ONLY public.hotels_hotel_amenities DROP CONSTRAINT hotels_hotel_amenities_hotel_id_fkey;
       public          postgres    false    216    231    3534            �           2606    16978 +   hotels_seasons hotels_seasons_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_seasons
    ADD CONSTRAINT hotels_seasons_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 U   ALTER TABLE ONLY public.hotels_seasons DROP CONSTRAINT hotels_seasons_hotel_id_fkey;
       public          postgres    false    225    3534    216            �           2606    16983 ,   hotels_seasons hotels_seasons_season_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_seasons
    ADD CONSTRAINT hotels_seasons_season_id_fkey FOREIGN KEY (season_id) REFERENCES public.seasons(id);
 V   ALTER TABLE ONLY public.hotels_seasons DROP CONSTRAINT hotels_seasons_season_id_fkey;
       public          postgres    false    224    3548    225            �           2606    16960 /   reservations reservations_primary_guest_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_primary_guest_id_fkey FOREIGN KEY (primary_guest_id) REFERENCES public.guests(id);
 Y   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_primary_guest_id_fkey;
       public          postgres    false    220    222    3544            �           2606    16955 &   reservations reservations_room_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id);
 P   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_room_id_fkey;
       public          postgres    false    222    218    3538            �           2606    16926    rooms rooms_hotel_id_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 C   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_hotel_id_fkey;
       public          postgres    false    3534    218    216            �           2606    17055 >   rooms_room_amenities rooms_room_amenities_room_amenity_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rooms_room_amenities
    ADD CONSTRAINT rooms_room_amenities_room_amenity_id_fkey FOREIGN KEY (room_amenity_id) REFERENCES public.room_amenities(id);
 h   ALTER TABLE ONLY public.rooms_room_amenities DROP CONSTRAINT rooms_room_amenities_room_amenity_id_fkey;
       public          postgres    false    233    234    3566            �           2606    17050 6   rooms_room_amenities rooms_room_amenities_room_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rooms_room_amenities
    ADD CONSTRAINT rooms_room_amenities_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id);
 `   ALTER TABLE ONLY public.rooms_room_amenities DROP CONSTRAINT rooms_room_amenities_room_id_fkey;
       public          postgres    false    234    3538    218            �   k   x�3��))JTp�����K�)-�,K�2�D�s:��($�(8�&f�%�p�p����(8�'�p�rz$�A9f`��y9�\�H��3K2�KK�v%�g��p��qqq l�(_      �   �   x�E�MO� E׏_��y(�cgM�ILLFwn^fP�ִ��/L����{�y
����)���B���R�Ӄ!!�f��"�Q���'�Kc��i��>F�Y���`]K���).A1�]�s��c�ԕ5�V���̫��&���������f����.3�BK ~��R=�*�10�c���7�[���w�B磻�n�'�u(�:�@��3@��g曅t���!c��Y�      �   l   x�%�1
�@�z�)��1�Z���66>a0��E�o �{'	����+3��O����張F��k��#Kc�E�޸��e2֙�J�7���A�Y��&�u �8z �      �   s  x�U�Mo�@���_������H�!U���4��^{�#۳hwJ}�?�>�S��pb!S�l��=<i�-�4�㢬��r�k�)�s�t�C��L-�b�(������_�FT��}����D��|0]�4.�2~\-�,rp�ɝQي_���9�IcǠ=�����bl�Q�PON1*���78�:lٓ�*U/�����k����h��4D��<�I&��y��릞v�V�Q
�$��<6�y#o����i~�WnYn�7�b�8>�VjM����qd���ݿף6��fC��̋U�}�8[\UET+�[۳��	l3�A�L=��\=�>vA�(��	����VZ+𓼎,>$��IE.�-      �   7   x���  ��������;�`9��5���2�j�26�|��'lc�ߒ�C_�      �   :   x���  �޹a�R�a�90<�+r�+,��-�e�(��6�
�؅l'��%�ig	>      �   /   x��� 0��pL%B�M���V�m�劺�E���h�ᗶ�{$=�\N      �   j   x�E���0C��.�lM�K���H�kO���rz4����	]�A���9�(o�?!��������ҹ1q���c�/�ZG���'K��%�q��q��ʳ�0�R� �      �   F   x�3�I�I-�,����2�����LJ,�2�tO�MUp��+��I�2�NLK�2�(��JM.�/����� cE�      �   :  x�U��n�0D���T�CzRD��S%d��HԖ�]�_�qI�"�b����]I���5��6����K9iARP�������LAZ�v��񑏛�%�*)A�j5���eU�H�Y����[{
7���Q�!���|R-׷ Y�	�Q��Kl�|a]8�xJ(��S������-/vC�i;j�D}�S7
8�n�?EJ�wG�ir�I��`�6t����8�B0�A濮�n���iXI=���^�������.�At{s�g�'�;j�g ���i�M� ,Rf��.�U������]��锺�P����7��y�      �   H   x����0���b�����@�ᑭ��\nAJA�0qiIJ�.ذ��[�ܰ7}�o��)��O��$}o�      �   g   x�3�.��M-R0202��f��P����!�gxf^	T�.�0��A���Yrse�+�� %�jLA��p�%�� ����04�56������ ���     