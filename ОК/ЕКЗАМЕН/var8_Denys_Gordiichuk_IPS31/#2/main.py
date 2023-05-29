import hashlib
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from Crypto.PublicKey import RSA
from Crypto.Signature import pkcs1_15
from Crypto.Hash import SHA256

# Generate RSA key pair
def generate_key_pair():
    key = RSA.generate(2048)
    private_key = key.export_key()
    public_key = key.publickey().export_key()
    return private_key, public_key

# Encrypt a message using AES
def encrypt_message(message, key):
    cipher = AES.new(key, AES.MODE_EAX)
    ciphertext, tag = cipher.encrypt_and_digest(message.encode('utf-8'))
    return cipher.nonce, tag, ciphertext

# Decrypt a message using AES
def decrypt_message(nonce, tag, ciphertext, key):
    cipher = AES.new(key, AES.MODE_EAX, nonce)
    plaintext = cipher.decrypt_and_verify(ciphertext, tag)
    return plaintext.decode('utf-8')

# Sign a message using RSA private key
def sign_message(message, private_key):
    key = RSA.import_key(private_key)
    h = SHA256.new(message.encode('utf-8'))
    signature = pkcs1_15.new(key).sign(h)
    return signature

# Verify the signature of a message using RSA public key
def verify_signature(message, signature, public_key):
    key = RSA.import_key(public_key)
    h = SHA256.new(message.encode('utf-8'))
    try:
        pkcs1_15.new(key).verify(h, signature)
        return True
    except (ValueError, TypeError):
        return False

# Generate key pair
private_key, public_key = generate_key_pair()
print("Private Key:\n", private_key)
print("Public Key:\n", public_key)

# Encrypt and decrypt a message
message = "Hello, World!"
key = get_random_bytes(16)  # 16-byte key for AES
nonce, tag, ciphertext = encrypt_message(message, key)
decrypted_message = decrypt_message(nonce, tag, ciphertext, key)
print("Decrypted Message:", decrypted_message)

# Sign and verify a message
signature = sign_message(message, private_key)
is_valid = verify_signature(message, signature, public_key)
print("Signature is valid:", is_valid)
