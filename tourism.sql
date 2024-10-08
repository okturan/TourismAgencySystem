PGDMP  )                    |            tourism_agency    16.3 (Postgres.app)    16.3 \    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
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
       public          postgres    false    224            �           0    0    board_types_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.board_types_id_seq OWNED BY public.board_types.id;
          public          postgres    false    223            �            1259    17013    hotel_amenities    TABLE     j   CREATE TABLE public.hotel_amenities (
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
       public          postgres    false    227            �           0    0    hotel_amenities_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.hotel_amenities_id_seq OWNED BY public.hotel_amenities.id;
          public          postgres    false    226            �            1259    16905    hotels    TABLE     �  CREATE TABLE public.hotels (
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
          public          postgres    false    215            �            1259    16945    reservations    TABLE     �  CREATE TABLE public.reservations (
    id integer NOT NULL,
    room_id integer NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    calculated_cost_usd numeric(7,2) NOT NULL,
    num_adults integer NOT NULL,
    num_children integer NOT NULL,
    full_name character varying(255),
    phone character varying(20),
    email character varying(255),
    identification character varying(50),
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
       public          postgres    false    220            �           0    0    reservations_id_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.reservations_id_seq OWNED BY public.reservations.id;
          public          postgres    false    219            �            1259    17037    room_amenities    TABLE     i   CREATE TABLE public.room_amenities (
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
       public          postgres    false    230            �           0    0    room_amenities_id_seq    SEQUENCE OWNED BY     O   ALTER SEQUENCE public.room_amenities_id_seq OWNED BY public.room_amenities.id;
          public          postgres    false    229            �            1259    17140    room_prices    TABLE     �  CREATE TABLE public.room_prices (
    id integer NOT NULL,
    room_id integer NOT NULL,
    season_id integer NOT NULL,
    board_type_id integer NOT NULL,
    adult_price_usd numeric(10,2) NOT NULL,
    child_price_usd numeric(10,2) NOT NULL,
    CONSTRAINT room_prices_adult_price_usd_check CHECK ((adult_price_usd >= (0)::numeric)),
    CONSTRAINT room_prices_child_price_usd_check CHECK ((child_price_usd >= (0)::numeric))
);
    DROP TABLE public.room_prices;
       public         heap    postgres    false            �            1259    17139    room_prices_id_seq    SEQUENCE     �   CREATE SEQUENCE public.room_prices_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.room_prices_id_seq;
       public          postgres    false    235            �           0    0    room_prices_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.room_prices_id_seq OWNED BY public.room_prices.id;
          public          postgres    false    234            �            1259    16915    rooms    TABLE     T  CREATE TABLE public.rooms (
    id integer NOT NULL,
    hotel_id integer NOT NULL,
    name character varying(255) NOT NULL,
    capacity integer NOT NULL,
    size_sqm integer NOT NULL,
    stock integer NOT NULL,
    room_type character varying(50),
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
       public         heap    postgres    false            �            1259    16966    seasons    TABLE     �  CREATE TABLE public.seasons (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    rate_multiplier numeric(3,0) DEFAULT 1 NOT NULL,
    hotel_id integer NOT NULL,
    CONSTRAINT seasons_check CHECK ((end_date > start_date)),
    CONSTRAINT seasons_rate_multiplier_check CHECK ((rate_multiplier > (0)::numeric))
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
       public          postgres    false    222            �           0    0    seasons_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.seasons_id_seq OWNED BY public.seasons.id;
          public          postgres    false    221            �            1259    17104    users_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.users_id_seq;
       public          postgres    false            �            1259    17105    users    TABLE     �  CREATE TABLE public.users (
    id integer DEFAULT nextval('public.users_id_seq'::regclass) NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    email character varying(255),
    role character varying(20) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['admin'::character varying, 'staff'::character varying])::text[])))
);
    DROP TABLE public.users;
       public         heap    postgres    false    232            �           2604    16992    board_types id    DEFAULT     p   ALTER TABLE ONLY public.board_types ALTER COLUMN id SET DEFAULT nextval('public.board_types_id_seq'::regclass);
 =   ALTER TABLE public.board_types ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    223    224    224            �           2604    17016    hotel_amenities id    DEFAULT     x   ALTER TABLE ONLY public.hotel_amenities ALTER COLUMN id SET DEFAULT nextval('public.hotel_amenities_id_seq'::regclass);
 A   ALTER TABLE public.hotel_amenities ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    227    226    227            �           2604    16908 	   hotels id    DEFAULT     f   ALTER TABLE ONLY public.hotels ALTER COLUMN id SET DEFAULT nextval('public.hotels_id_seq'::regclass);
 8   ALTER TABLE public.hotels ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215    216            �           2604    16948    reservations id    DEFAULT     r   ALTER TABLE ONLY public.reservations ALTER COLUMN id SET DEFAULT nextval('public.reservations_id_seq'::regclass);
 >   ALTER TABLE public.reservations ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    220    220            �           2604    17040    room_amenities id    DEFAULT     v   ALTER TABLE ONLY public.room_amenities ALTER COLUMN id SET DEFAULT nextval('public.room_amenities_id_seq'::regclass);
 @   ALTER TABLE public.room_amenities ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    230    229    230            �           2604    17143    room_prices id    DEFAULT     p   ALTER TABLE ONLY public.room_prices ALTER COLUMN id SET DEFAULT nextval('public.room_prices_id_seq'::regclass);
 =   ALTER TABLE public.room_prices ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    234    235    235            �           2604    16918    rooms id    DEFAULT     d   ALTER TABLE ONLY public.rooms ALTER COLUMN id SET DEFAULT nextval('public.rooms_id_seq'::regclass);
 7   ALTER TABLE public.rooms ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    218    218            �           2604    16969 
   seasons id    DEFAULT     h   ALTER TABLE ONLY public.seasons ALTER COLUMN id SET DEFAULT nextval('public.seasons_id_seq'::regclass);
 9   ALTER TABLE public.seasons ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    222    221    222            �          0    16989    board_types 
   TABLE DATA           /   COPY public.board_types (id, name) FROM stdin;
    public          postgres    false    224   s       �          0    17013    hotel_amenities 
   TABLE DATA           3   COPY public.hotel_amenities (id, name) FROM stdin;
    public          postgres    false    227   �s       �          0    16905    hotels 
   TABLE DATA           f   COPY public.hotels (id, name, phone, email, stars, address_line, country, city, district) FROM stdin;
    public          postgres    false    216   �s       �          0    16997    hotels_board_types 
   TABLE DATA           E   COPY public.hotels_board_types (hotel_id, board_type_id) FROM stdin;
    public          postgres    false    225   �u       �          0    17021    hotels_hotel_amenities 
   TABLE DATA           L   COPY public.hotels_hotel_amenities (hotel_id, hotel_amenity_id) FROM stdin;
    public          postgres    false    228   �u       �          0    16945    reservations 
   TABLE DATA           �   COPY public.reservations (id, room_id, start_date, end_date, calculated_cost_usd, num_adults, num_children, full_name, phone, email, identification) FROM stdin;
    public          postgres    false    220   Mv       �          0    17037    room_amenities 
   TABLE DATA           2   COPY public.room_amenities (id, name) FROM stdin;
    public          postgres    false    230   �v       �          0    17140    room_prices 
   TABLE DATA           n   COPY public.room_prices (id, room_id, season_id, board_type_id, adult_price_usd, child_price_usd) FROM stdin;
    public          postgres    false    235   Bw       �          0    16915    rooms 
   TABLE DATA           Y   COPY public.rooms (id, hotel_id, name, capacity, size_sqm, stock, room_type) FROM stdin;
    public          postgres    false    218   �w       �          0    17045    rooms_room_amenities 
   TABLE DATA           H   COPY public.rooms_room_amenities (room_id, room_amenity_id) FROM stdin;
    public          postgres    false    231   qy       �          0    16966    seasons 
   TABLE DATA           \   COPY public.seasons (id, name, start_date, end_date, rate_multiplier, hotel_id) FROM stdin;
    public          postgres    false    222   �y       �          0    17105    users 
   TABLE DATA           [   COPY public.users (id, username, password, first_name, last_name, email, role) FROM stdin;
    public          postgres    false    233   �z       �           0    0    board_types_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.board_types_id_seq', 8, true);
          public          postgres    false    223            �           0    0    hotel_amenities_id_seq    SEQUENCE SET     D   SELECT pg_catalog.setval('public.hotel_amenities_id_seq', 8, true);
          public          postgres    false    226            �           0    0    hotels_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.hotels_id_seq', 16, true);
          public          postgres    false    215            �           0    0    reservations_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.reservations_id_seq', 11, true);
          public          postgres    false    219            �           0    0    room_amenities_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.room_amenities_id_seq', 6, true);
          public          postgres    false    229            �           0    0    room_prices_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.room_prices_id_seq', 44, true);
          public          postgres    false    234            �           0    0    rooms_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.rooms_id_seq', 32, true);
          public          postgres    false    217            �           0    0    seasons_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.seasons_id_seq', 14, true);
          public          postgres    false    221            �           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 3, true);
          public          postgres    false    232            �           2606    16996     board_types board_types_name_key 
   CONSTRAINT     [   ALTER TABLE ONLY public.board_types
    ADD CONSTRAINT board_types_name_key UNIQUE (name);
 J   ALTER TABLE ONLY public.board_types DROP CONSTRAINT board_types_name_key;
       public            postgres    false    224            �           2606    16994    board_types board_types_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.board_types
    ADD CONSTRAINT board_types_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.board_types DROP CONSTRAINT board_types_pkey;
       public            postgres    false    224            �           2606    17020 (   hotel_amenities hotel_amenities_name_key 
   CONSTRAINT     c   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT hotel_amenities_name_key UNIQUE (name);
 R   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT hotel_amenities_name_key;
       public            postgres    false    227            �           2606    17018 $   hotel_amenities hotel_amenities_pkey 
   CONSTRAINT     b   ALTER TABLE ONLY public.hotel_amenities
    ADD CONSTRAINT hotel_amenities_pkey PRIMARY KEY (id);
 N   ALTER TABLE ONLY public.hotel_amenities DROP CONSTRAINT hotel_amenities_pkey;
       public            postgres    false    227            �           2606    17001 *   hotels_board_types hotels_board_types_pkey 
   CONSTRAINT     }   ALTER TABLE ONLY public.hotels_board_types
    ADD CONSTRAINT hotels_board_types_pkey PRIMARY KEY (hotel_id, board_type_id);
 T   ALTER TABLE ONLY public.hotels_board_types DROP CONSTRAINT hotels_board_types_pkey;
       public            postgres    false    225    225            �           2606    17025 2   hotels_hotel_amenities hotels_hotel_amenities_pkey 
   CONSTRAINT     �   ALTER TABLE ONLY public.hotels_hotel_amenities
    ADD CONSTRAINT hotels_hotel_amenities_pkey PRIMARY KEY (hotel_id, hotel_amenity_id);
 \   ALTER TABLE ONLY public.hotels_hotel_amenities DROP CONSTRAINT hotels_hotel_amenities_pkey;
       public            postgres    false    228    228            �           2606    16913    hotels hotels_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.hotels
    ADD CONSTRAINT hotels_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.hotels DROP CONSTRAINT hotels_pkey;
       public            postgres    false    216            �           2606    16954    reservations reservations_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_pkey;
       public            postgres    false    220            �           2606    17044 &   room_amenities room_amenities_name_key 
   CONSTRAINT     a   ALTER TABLE ONLY public.room_amenities
    ADD CONSTRAINT room_amenities_name_key UNIQUE (name);
 P   ALTER TABLE ONLY public.room_amenities DROP CONSTRAINT room_amenities_name_key;
       public            postgres    false    230            �           2606    17042 "   room_amenities room_amenities_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.room_amenities
    ADD CONSTRAINT room_amenities_pkey PRIMARY KEY (id);
 L   ALTER TABLE ONLY public.room_amenities DROP CONSTRAINT room_amenities_pkey;
       public            postgres    false    230            �           2606    17147    room_prices room_prices_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.room_prices
    ADD CONSTRAINT room_prices_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.room_prices DROP CONSTRAINT room_prices_pkey;
       public            postgres    false    235            �           2606    16925    rooms rooms_name_hotel_id_key 
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
       public            postgres    false    231    231            �           2606    16972    seasons seasons_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.seasons
    ADD CONSTRAINT seasons_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.seasons DROP CONSTRAINT seasons_pkey;
       public            postgres    false    222            �           2606    17117    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public            postgres    false    233            �           2606    17113    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    233            �           2606    17115    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public            postgres    false    233            �           2606    17158    room_prices fk_board_type    FK CONSTRAINT     �   ALTER TABLE ONLY public.room_prices
    ADD CONSTRAINT fk_board_type FOREIGN KEY (board_type_id) REFERENCES public.board_types(id);
 C   ALTER TABLE ONLY public.room_prices DROP CONSTRAINT fk_board_type;
       public          postgres    false    235    3551    224                        2606    17148    room_prices fk_room    FK CONSTRAINT     r   ALTER TABLE ONLY public.room_prices
    ADD CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES public.rooms(id);
 =   ALTER TABLE ONLY public.room_prices DROP CONSTRAINT fk_room;
       public          postgres    false    235    218    3543                       2606    17153    room_prices fk_season    FK CONSTRAINT     x   ALTER TABLE ONLY public.room_prices
    ADD CONSTRAINT fk_season FOREIGN KEY (season_id) REFERENCES public.seasons(id);
 ?   ALTER TABLE ONLY public.room_prices DROP CONSTRAINT fk_season;
       public          postgres    false    3547    235    222            �           2606    17007 8   hotels_board_types hotels_board_types_board_type_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_board_types
    ADD CONSTRAINT hotels_board_types_board_type_id_fkey FOREIGN KEY (board_type_id) REFERENCES public.board_types(id);
 b   ALTER TABLE ONLY public.hotels_board_types DROP CONSTRAINT hotels_board_types_board_type_id_fkey;
       public          postgres    false    224    225    3551            �           2606    17002 3   hotels_board_types hotels_board_types_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_board_types
    ADD CONSTRAINT hotels_board_types_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 ]   ALTER TABLE ONLY public.hotels_board_types DROP CONSTRAINT hotels_board_types_hotel_id_fkey;
       public          postgres    false    225    216    3539            �           2606    17031 C   hotels_hotel_amenities hotels_hotel_amenities_hotel_amenity_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_hotel_amenities
    ADD CONSTRAINT hotels_hotel_amenities_hotel_amenity_id_fkey FOREIGN KEY (hotel_amenity_id) REFERENCES public.hotel_amenities(id);
 m   ALTER TABLE ONLY public.hotels_hotel_amenities DROP CONSTRAINT hotels_hotel_amenities_hotel_amenity_id_fkey;
       public          postgres    false    228    227    3557            �           2606    17026 ;   hotels_hotel_amenities hotels_hotel_amenities_hotel_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.hotels_hotel_amenities
    ADD CONSTRAINT hotels_hotel_amenities_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 e   ALTER TABLE ONLY public.hotels_hotel_amenities DROP CONSTRAINT hotels_hotel_amenities_hotel_id_fkey;
       public          postgres    false    3539    228    216            �           2606    16955 &   reservations reservations_room_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.reservations
    ADD CONSTRAINT reservations_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id);
 P   ALTER TABLE ONLY public.reservations DROP CONSTRAINT reservations_room_id_fkey;
       public          postgres    false    218    220    3543            �           2606    16926    rooms rooms_hotel_id_fkey    FK CONSTRAINT     z   ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 C   ALTER TABLE ONLY public.rooms DROP CONSTRAINT rooms_hotel_id_fkey;
       public          postgres    false    3539    218    216            �           2606    17055 >   rooms_room_amenities rooms_room_amenities_room_amenity_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rooms_room_amenities
    ADD CONSTRAINT rooms_room_amenities_room_amenity_id_fkey FOREIGN KEY (room_amenity_id) REFERENCES public.room_amenities(id);
 h   ALTER TABLE ONLY public.rooms_room_amenities DROP CONSTRAINT rooms_room_amenities_room_amenity_id_fkey;
       public          postgres    false    230    231    3563            �           2606    17050 6   rooms_room_amenities rooms_room_amenities_room_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.rooms_room_amenities
    ADD CONSTRAINT rooms_room_amenities_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id);
 `   ALTER TABLE ONLY public.rooms_room_amenities DROP CONSTRAINT rooms_room_amenities_room_id_fkey;
       public          postgres    false    3543    218    231            �           2606    17119    seasons seasons_hotel_id_fkey    FK CONSTRAINT     ~   ALTER TABLE ONLY public.seasons
    ADD CONSTRAINT seasons_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);
 G   ALTER TABLE ONLY public.seasons DROP CONSTRAINT seasons_hotel_id_fkey;
       public          postgres    false    216    3539    222            �   k   x�3��))JTp�����K�)-�,K�2�D�s:��($�(8�&f�%�p�p����(8�'�p�rz$�A9f`��y9�\�H��3K2�KK�v%�g��p��qqq l�(_      �   l   x�%�1
�@�z�)��1�Z���66>a0��E�o �{'	����+3��O����張F��k��#Kc�E�޸��e2֙�J�7���A�Y��&�u �8z �      �   �  x�U��n�@����;2��r#��T�4��\�7�g��D����-y�.r����?��p��*j��V�	Y��8I�q�O�`xg��S�6ò�1��TlOj�LP���&� k�E�Ex���RXَ�gC�[D�y�ͦ�$���ȓ;��~�^��Kۆ��C�/-�,jX �Fx@��B�-9�(��cO��J=��NnA�o�=��pW�𯾗���8s$��FH�BP���`k���T�Qg��F�ѡ6��WfL&�tr�/x�˕<\��T�A2J��i����#�Th���UW����>JX�Ue�������L^=�]��5�`ӹ����7��k�폯��it�F�jA,��co6Ȳ8]�fqf[kk�{?/[�hg�Ր�tv�;
�8
��|��--k�𓼴�}PF/�(���<�%      �   8   x��� 1��0L���n��?G�2¢�.m2n�
��U.l������Ngپ*jx      �   ?   x���  ���0H)-,��s`xX9�+#�5��傭⯘��x�n�d�4�߲��7ϐt�9      �   �   x�m�M
�@��/w鐿2v����""�� ��ck��l�"ߗ���zù��Bܙ3���?`Zf��bn괃ta�0d�i-̇>?_���_��4�ʜ�l���R��Wu�U��D���[���l=<�o��X_�-�v>&"zK�A�      �   F   x�3�I�I-�,����2�����LJ,�2�tO�MUp��+��I�2�NLK�2�(��JM.�/����� cE�      �   �   x�m�K� C��0��.��9�؞.D60��BB�n拹��I�%5���9x��܁�O���^���6��1W��iGW�N�~��q��1���8�=m�\���-D�����F�l�������Ng��/���]c�<y�E�#'���T����?��_V      �   l  x�eR�n�0<o��_P%N�ʣ�
"MO��[�dl��<��]'A"����ٙٱ��T�*�Uᅭ�>��=Ί�|�Yg�"x�F!Ѳ�U���y1��}���~�V����/C�A�r��76�!��P{-UB��.����i���J�j���t���37�Beڝ|{� ��(ۢh�+�$���ޱD↸��l9���i~�<3!ҧ��_�}1��Z�3"O�gi�Y"�(��:���3�¨�R׿7�Wj/���?��,�SVT��DY)J|�;3=�2�����mzu]��K�E��N��^I!�������ۈ���W�B��K��v��Z�����}�O����S,iW��%�?����      �   `   x���� c�� �������^Ɇ���4�bH%9�B�����Ң[t������A�f/��N�:܇n�&N{���K�E��W�G�\�      �   �   x�E�1�0Eg���+�	��\��X�dU�(�p�B	���������%Ds�:z�eu�BR�h�DE��k����;:�=�+h�8�/S�\h���23X��$���XI+��tXB�������1����:��A�L���M�F��/���?J/6u      �   N   x�3�LL��̃��%�ى�.�e�`�C
�	��2�,.ILK���ىy��E%�E@���s3s���s!��b���� cV �     