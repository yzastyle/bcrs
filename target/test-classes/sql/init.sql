CREATE TYPE card_status AS ENUM ('ACTIVE', 'BLOCKED', 'EXPIRED');

CREATE TABLE users (
    id UUID PRIMARY KEY NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(5) NOT NULL,
    date_create TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE cards (
    id UUID PRIMARY KEY NOT NULL,
    number VARCHAR(16) NOT NULL,
    owner VARCHAR(255) NOT NULL,
    expiration_date VARCHAR(5) NOT NULL,
    status card_status NOT NULL,
    deposit DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    user_id UUID NOT NULL,
    date_create TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE cards
  ADD CONSTRAINT fk_cards_users_user_id
  FOREIGN KEY (user_id)
  REFERENCES users (id)
  ON DELETE CASCADE;

INSERT INTO users (id, login, name, password, role)
VALUES
  ('d17ba058-3684-41cc-9cdb-3ea95d009000', 'findCardsByCriteriaTestEmpty', 'findCardsByCriteriaTestEmpty', 'password', 'USER'),
  ('d17ba058-3684-41cc-9cdb-3ea95d0a9d6f', 'alice',             'Alice Johnson',             'password', 'USER'),
  ('d17ba058-3684-41cc-9fdb-3ea95d0a9d6f', 'Sash_1',            'Sash_1',                    'password', 'USER'),
  ('d17ba058-3684-41cc-9cfb-3ea95d0a9d6f', 'Sash_2',            'Sash_2',                    'password', 'USER'),
  ('d17ba058-3684-41cc-9cdf-3ea95d0a9d6f', 'Sash_3',            'Sash_3',                    'password', 'USER'),
  ('58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a', 'bob',               'Bob Smith',                 'password', 'USER'),
  ('11111ab1-15a3-4a5f-8f0c-1a2dfc80cc6a', 'new bob',           'new Bob Smith',             'password', 'USER'),
  ('11111ab1-15a3-4a5f-8f0c-1a2df111cc6a', 'new bob_3',         'new Bob Smith_3',           'password', 'USER'),
  ('11111ab1-15a3-4a5f-8f0c-23456111cc6a', 'new bob_4',         'new Bob Smith_4',           'password', 'USER'),
  ('82b547fa-18c6-4d40-b497-a1642e8aac2c', 'new bob_5',         'new Bob Smith_5',           'password', 'USER'),
  ('82b547fa-18c6-4d40-b497-a1643f8aac2c', 'new bob_6',         'new Bob Smith_6',           'password', 'USER'),
  ('10f80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a', 'deleteCardsByIdPositiveTest', 'deleteCardsByIdPositiveTest', 'password', 'USER'),
  ('aaaaaab1-15a3-4a5f-8f0c-1a2dfc80cc6a', 'deleteCardByIdPositiveTest',   'deleteCardByIdPositiveTest',   'password', 'USER'),
  ('aaaaaa24-15a3-4a5f-8f0c-1a2dfc80cc6a', 'deleteCardByIdNegativeTest',   'deleteCardByIdNegativeTest',   'password', 'USER'),
  ('aaaaaa24-15a3-1a23-8f0c-1a2dfc80cc6a', 'getDepositByCardIdTest',      'getDepositByCardIdTest',      'password', 'USER'),
  ('aaaaaa24-15a3-1a23-8f0c-1a2dfc03ff6a', 'getDepositByCardIdTest',      'getDepositByCardIdTest',      'password', 'USER');

INSERT INTO cards (id, number, owner, expiration_date, status, deposit, user_id)
VALUES
(
    'faad7771-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'deleteCardsByIdPositiveTest',
    '12/25',
    'ACTIVE',
    1000.00,
    '82b547fa-18c6-4d40-b497-a1643f8aac2c'
  ),
  (
      'faad8889-46d7-4264-8570-df21b68ed5ff',
      '4111222233334444',
      'deleteCardsByIdPositiveTest',
      '12/25',
      'ACTIVE',
      1000.00,
      '82b547fa-18c6-4d40-b497-a1643f8aac2c'
    ),
(
    'faad0001-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'deleteCardsByIdPositiveTest',
    '12/25',
    'ACTIVE',
    1000.00,
    'aaaaaa24-15a3-1a23-8f0c-1a2dfc80cc6a'
  ),
(
    'fcad0000-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'deleteCardsByIdPositiveTest',
    '12/25',
    'ACTIVE',
    1000.00,
    'aaaaaa24-15a3-1a23-8f0c-1a2dfc03ff6a'
  ),
(
    'aaad0001-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'deleteCardsByIdPositiveTest',
    '12/25',
    'ACTIVE',
    1000.00,
    'aaaaaab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
  ),
(
    'aaad0000-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'deleteCardsByIdPositiveTest',
    '12/25',
    'ACTIVE',
    1000.00,
    'aaaaaab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
  ),
  (
      'aaad0000-46d7-4264-8137-df21b68ed5ff',
      '4111222233334444',
      'deleteCardsByIdPositiveTest',
      '12/25',
      'ACTIVE',
      0,
      'aaaaaab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
    ),
(
    'aaad0496-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'deleteCardsByIdPositiveTest',
    '12/25',
    'ACTIVE',
    1000.00,
    '10f80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
  ),
  (
      'bbbd0496-46d7-4264-8570-df21b68ed5ff',
      '4111222233334444',
      'deleteCardsByIdPositiveTest',
      '12/25',
      'ACTIVE',
      1000.00,
      '10f80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
    ),
      (
          'bbbd0496-35d0-3334-8570-df21b68ed5ff',
          '4111222233334444',
          'deleteCardsByIdPositiveTest',
          '12/25',
          'ACTIVE',
          0,
          '10f80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
        ),
    (
          'bbbd0496-46d7-1155-6670-df21b68ed5ff',
          '4111222233334444',
          'deleteCardsByIdPositiveTest',
          '12/25',
          'ACTIVE',
          0.00,
          '10f80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
        ),
  (
    'ff8d0496-46d7-4264-8570-df21b68ed5ff',
    '4111222233334444',
    'Alice Johnson',
    '12/25',
    'ACTIVE',
    1000.00,
    'd17ba058-3684-41cc-9cdb-3ea95d0a9d6f'
  ),
  (
    '1b52679d-1c73-46a2-95ff-0ea5756f2513',
    '5555666677778888',
    'Alice Johnson',
    '11/24',
    'EXPIRED',
    0.00,
    'd17ba058-3684-41cc-9cdb-3ea95d0a9d6f'
  ),
    (
      '1b52679d-1c73-46a2-11ff-1ea5756f2513',
      '5555666677778828',
      'Alice Johnson',
      '11/24',
      'EXPIRED',
      0.00,
      'd17ba058-3684-41cc-9cdb-3ea95d0a9d6f'
    ),
  (
      '1b52679d-1c11-66a2-95ff-0ea5756f2513',
      '5555666617778888',
      'Alice Johnson',
      '11/24',
      'EXPIRED',
      0.00,
      'd17ba058-3684-41cc-9cdb-3ea95d009000'
    ),
  (
      '1f8d0413-46d7-4264-8570-df21b68ed5ff',
      '4111222233334444',
      'Alice Johnson',
      '12/25',
      'ACTIVE',
      1000.00,
      'd17ba058-3684-41cc-9cdb-3ea95d0a9d6f'
    ),
    (
        '1b52679d-1c89-46a2-95ff-0ea5756f2513',
        '5555656677778888',
        'Alice Johnson',
        '11/24',
        'EXPIRED',
        0.00,
        'd17ba058-3684-41cc-9cdb-3ea95d009000'
      ),
      (
          '1f8d0496-46d7-1235-fa70-df21b68ed5ff',
          '4111222233334444',
          'Alice Johnson',
          '12/25',
          'ACTIVE',
          1000.00,
          'd17ba058-3684-41cc-9cdb-3ea95d009000'
        ),
        (
            '1b52679d-1c73-16ff-05ff-0ea5756f2513',
            '5555366677778888',
            'Alice Johnson',
            '11/24',
            'BLOCKED',
            0.00,
            'd17ba058-3684-41cc-9cdb-3ea95d009000'
          ),
          (
              '1f8d0496-46d7-4264-8570-df21b68ed5ff',
              '4111222233334444',
              'Alice Johnson',
              '12/24',
              'ACTIVE',
              1000.00,
              'd17ba058-3684-41cc-9cdb-3ea95d009000'
            ),
            (
                          '99856168-571c-4b5a-a7a2-4ae941c42f1d',
                          '4111222233334444',
                          'Alice Johnson',
                          '12/24',
                          'ACTIVE',
                          1000.00,
                          'd17ba058-3684-41cc-9cdb-3ea95d009000'
                        ),
            (
                          'eda75a38-897d-4ec3-bc0c-10266170f44b',
                          '4111222233334444',
                          'Alice Johnson',
                          '12/24',
                          'ACTIVE',
                          1000.00,
                          'd17ba058-3684-41cc-9cdb-3ea95d009000'
                        ),
            (
                          '287b6fde-fe19-4b31-8806-fc5aec11dc53',
                          '4111222233334444',
                          'Alice Johnson',
                          '12/24',
                          'ACTIVE',
                          1000.00,
                          'd17ba058-3684-41cc-9cdb-3ea95d009000'
                        ),
            (
                          'f403fe36-aba4-47b7-8abf-4ea0a2d56f53',
                          '4111222233334444',
                          'Alice Johnson',
                          '12/24',
                          'ACTIVE',
                          1000.00,
                          'd17ba058-3684-41cc-9cdb-3ea95d009000'
                        ),
            (
                          'd219d113-8e76-4160-bf4e-729aa56c1e55',
                          '4111222233334444',
                          'Alice Johnson',
                          '12/24',
                          'ACTIVE',
                          1000.00,
                          'd17ba058-3684-41cc-9cdb-3ea95d009000'
                        ),
            (
                          'de270e74-50f2-427f-8244-ea2c89d07f8e',
                          '4111222233334444',
                          'Alice Johnson',
                          '12/24',
                          'ACTIVE',
                          1000.00,
                          'd17ba058-3684-41cc-9cdb-3ea95d009000'
                        ),
    (
      '2b52679d-1c73-46a2-95ff-0ea5756f2513',
      '5555666677778881',
      'Alice Johnson',
      '11/24',
      'EXPIRED',
      0.00,
      'd17ba058-3684-41cc-9cdb-3ea95d0a9d6f'
    ),
(
      'f909df0e-8ca5-4f09-8bc0-43540de2f3ea',
      '5555666677778882',
      'Alice Johnson',
      '06/25',
      'ACTIVE',
      1000,
      '11111ab1-15a3-4a5f-8f0c-23456111cc6a'
    ),
(
      '9ee5ca3b-c707-4a33-97c9-d42aabaf4959',
      '5555666677778883',
      'Alice Johnson',
      '05/25',
      'ACTIVE',
      1000,
      '11111ab1-15a3-4a5f-8f0c-23456111cc6a'
    ),
    (
          'b351ba45-f03b-48dd-ac62-c9f4fbae2707',
          '5555666677778884',
          'Alice Johnson',
          '06/25',
          'ACTIVE',
          1000,
          '82b547fa-18c6-4d40-b497-a1642e8aac2c'
        ),
    (
          'dcd57932-0e43-4ae4-94eb-07a2bff90f29',
          '5555666677778885',
          'Alice Johnson',
          '06/25',
          'ACTIVE',
          1000.01,
          '82b547fa-18c6-4d40-b497-a1642e8aac2c'
        );


INSERT INTO cards (id, number, owner, expiration_date, status, deposit, user_id)
VALUES
  (
    '16a9dd86-198c-4b42-8a42-bbb86de93287',
    '9999000011112222',
    'Bob Smith',
    '08/26',
    'ACTIVE',
    500.50,
    '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
  ),
  (
      '11a9dd86-198c-4b42-8a42-bbb86de93287',
      '9009000011112222',
      'Bob Smith',
      '08/26',
      'ACTIVE',
      500.50,
      '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
    ),
    (
          '11a9dd86-198c-4b42-8a00-bbb86de93287',
          '9009000011112222',
          'Bob Smith',
          '08/26',
          'ACTIVE',
          500.50,
          '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
    ),
    (
              '11a9dd86-198c-4b42-0a00-bbb86de93287',
              '9009000011112222',
              'Bob Smith',
              '08/26',
              'ACTIVE',
              500.50,
              '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
    ),
  (
    '9c05a73f-38a4-4736-91b0-7447d48fb70f',
    '3333444455556666',
    'Bob Smith',
    '01/23',
    'ACTIVE',
    250.75,
    '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
  ),
  (
      '1c05a73f-38a4-4736-91b0-7447d48fb70f',
      '3333444455556666',
      'Bob Smith',
      '01/23',
      'ACTIVE',
      250.75,
      '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
    ),
    (
        '9c05a73f-38a4-4736-91bf-7447d48fb70f',
        '3333444455556666',
        'Bob Smith',
        '01/23',
        'BLOCKED',
        250.75,
        '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
      ),
          (
              '9c05a73f-38a4-4736-91bf-7117d48fb70f',
              '3333444455556666',
              'Bob Smith',
              '01/23',
              'ACTIVE',
              250.75,
              '58c80ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
            ),
                (
                    '9c05a73f-38a4-4736-91bf-0047d48fb70f',
                    '3333444455556666',
                    'Bob Smith',
                    '01/23',
                    'ACTIVE',
                    250.75,
                    '11111ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
                  ),
                  (
                                      '9c05a73f-38a4-0000-91bf-0047d48fb70f',
                                      '3333444455556666',
                                      'Bob Smith',
                                      '01/23',
                                      'ACTIVE',
                                      250.75,
                                      '11111ab1-15a3-4a5f-8f0c-1a2dfc80cc6a'
                                    ),
(
                    '9c05a73f-38a4-0000-91bf-0047dddfb70f',
                    '3333466655556666',
                    'Bob Smith',
                    '01/27',
                    'ACTIVE',
                    250.75,
                    '11111ab1-15a3-4a5f-8f0c-1a2df111cc6a'
                  ),
                  (
                                      '9c05a73f-11a1-0000-91bf-0047d48fb70f',
                                      '3333442455556666',
                                      'Bob Smith',
                                      '01/27',
                                      'ACTIVE',
                                      250.75,
                                      '11111ab1-15a3-4a5f-8f0c-1a2df111cc6a'
                                    );
