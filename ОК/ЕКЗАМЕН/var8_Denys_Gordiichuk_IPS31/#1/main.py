import random
from math import gcd


def generate_keys():
    # Step 1: Choose a large prime number p
    p = generate_large_prime()

    # Step 2: Choose a primitive root g modulo p
    g = find_primitive_root(p)

    # Step 3: Choose a random private key x
    x = random.randint(2, p - 2)

    # Step 4: Compute the corresponding public key y
    y = pow(g, x, p)

    return p, g, y, x


def encrypt(plaintext, p, g, y):
    # Step 1: Choose a random secret key k
    k = random.randint(2, p - 2)

    # Step 2: Compute the temporary values a and b
    a = pow(g, k, p)
    b = (plaintext * pow(y, k, p)) % p

    return a, b


def decrypt(a, b, p, x):
    # Step 1: Compute the shared secret s
    s = pow(a, x, p)

    # Step 2: Compute the modular inverse of s
    s_inverse = modular_inverse(s, p)

    # Step 3: Decrypt the ciphertext
    plaintext = (b * s_inverse) % p

    return plaintext


def generate_large_prime():
    while True:
        prime_candidate = random.randint(2 ** 10, 2 ** 11)
        if is_prime(prime_candidate):
            return prime_candidate


def is_prime(n):
    if n == 2 or n == 3:
        return True
    if n <= 1 or n % 2 == 0:
        return False
    if n <= 3:
        return True
    i = 5
    while i * i <= n:
        if n % i == 0 or n % (i + 2) == 0:
            return False
        i += 6
    return True


def find_primitive_root(p):
    # Step 1: Calculate the Euler totient of p
    phi = p - 1

    # Step 2: Find the prime factors of phi
    prime_factors = factorize(phi)

    # Step 3: Find a primitive root g
    for g in range(2, p):
        if all(pow(g, phi // prime, p) != 1 for prime in prime_factors):
            return g

    return None


def factorize(n):
    factors = []
    while n % 2 == 0:
        factors.append(2)
        n //= 2
    p = 3
    while p * p <= n:
        if n % p == 0:
            factors.append(p)
            n //= p
        else:
            p += 2
    if n > 1:
        factors.append(n)
    return factors


def modular_inverse(a, m):
    if gcd(a, m) != 1:
        raise ValueError("The numbers are not coprime.")
    _, x, _ = extended_gcd(a, m)
    return x % m


def extended_gcd(a, b):
    if b == 0:
        return a, 1, 0
    gcd, x, y = extended_gcd(b, a % b)
    return gcd, y, x - (a // b) * y


# Demonstration
p, g, y, x = generate_keys()

print("Public Key (p, g, y):", (p, g, y))
print("Private Key (x):", x)

plaintext = int(input("Enter a number to encrypt: "))
a, b = encrypt(plaintext, p, g, y)
print("Ciphertext (a, b):", (a, b))

decrypted_plaintext = decrypt(a, b, p, x)
print("Decrypted Plaintext:", decrypted_plaintext)
