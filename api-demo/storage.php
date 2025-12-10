<?php
// storage.php

// File-based storage path
define('STORE_FILE', __DIR__ . '/data_store.json');

/**
 * Initialize and return a reference to the store.
 * Data lives in a JSON file (persists across all requests without sessions).
 */
function &get_store()
{
    static $store = null;
    
    if ($store === null) {
        if (file_exists(STORE_FILE)) {
            $content = file_get_contents(STORE_FILE);
            $store = json_decode($content, true);
        }
        
        if (!$store || !isset($store['books'])) {
            $store = [
                'nextBookId'       => 1,
                'books'            => [],
                'rateLimitedCount' => 0,
            ];
            // Seed some demo data
            seed_demo_data($store);
            save_store($store);
        }
    }

    return $store;
}

/**
 * Save the store to file.
 */
function save_store($store)
{
    file_put_contents(STORE_FILE, json_encode($store, JSON_PRETTY_PRINT));
}

function seed_demo_data(&$store)
{
    $now = date('c');
    $store['books'] = [
        1 => [
            'id'        => 1,
            'title'     => 'Clean Code',
            'author'    => 'Robert C. Martin',
            'price'     => 450.0,
            'stock'     => 10,
            'createdAt' => $now,
        ],
        2 => [
            'id'        => 2,
            'title'     => 'Effective Java',
            'author'    => 'Joshua Bloch',
            'price'     => 600.0,
            'stock'     => 5,
            'createdAt' => $now,
        ],
    ];
    $store['nextBookId'] = 3;
}

/** Completely reset all data (for tests). */
function reset_store()
{
    if (file_exists(STORE_FILE)) {
        unlink(STORE_FILE);
    }
    // Force re-init on next get_store() call
    $store = [
        'nextBookId'       => 1,
        'books'            => [],
        'rateLimitedCount' => 0,
    ];
    seed_demo_data($store);
    save_store($store);
}

/** Get single book by ID or null. */
function find_book($id)
{
    $store = &get_store();
    return $store['books'][$id] ?? null;
}

/** Save (create) a new book and return it. */
function create_book_in_store(array $data)
{
    $store = &get_store();
    $id = $store['nextBookId']++;

    $book = [
        'id'        => $id,
        'title'     => $data['title'],
        'author'    => $data['author'],
        'price'     => (float) $data['price'],
        'stock'     => (int) ($data['stock'] ?? 0),
        'createdAt' => date('c'),
    ];

    $store['books'][$id] = $book;
    save_store($store);
    return $book;
}

/** Update an existing book (full or partial). */
function update_book_in_store($id, array $data, $partial = false)
{
    $store = &get_store();

    if (!isset($store['books'][$id])) {
        return null;
    }

    $book = $store['books'][$id];

    if ($partial) {
        // PATCH – only update provided fields
        if (isset($data['title']))  $book['title']  = $data['title'];
        if (isset($data['author'])) $book['author'] = $data['author'];
        if (isset($data['price']))  $book['price']  = (float) $data['price'];
        if (isset($data['stock']))  $book['stock']  = (int) $data['stock'];
    } else {
        // PUT – replace main fields
        $book['title']  = $data['title'];
        $book['author'] = $data['author'];
        $book['price']  = (float) $data['price'];
        $book['stock']  = (int) ($data['stock'] ?? 0);
    }

    $store['books'][$id] = $book;
    save_store($store);
    return $book;
}

/** Delete a book. Returns true if deleted. */
function delete_book_in_store($id)
{
    $store = &get_store();

    if (!isset($store['books'][$id])) {
        return false;
    }

    unset($store['books'][$id]);
    save_store($store);
    return true;
}

/** List all books (we’ll filter/paginate in helpers). */
function list_all_books()
{
    $store = &get_store();
    return array_values($store['books']); // reindex
}

/** Simple counter for rate-limit demo. */
function increment_rate_counter()
{
    $store = &get_store();
    $store['rateLimitedCount']++;
    save_store($store);
    return $store['rateLimitedCount'];
}

function reset_rate_counter()
{
    $store = &get_store();
    $store['rateLimitedCount'] = 0;
    save_store($store);
}
