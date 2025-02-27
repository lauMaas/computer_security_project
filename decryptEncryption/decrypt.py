import binascii

hex_data = "6c7026a72726720706464762c206c70206f6478756c710a"

# Fix odd-length hex strings
if len(hex_data) % 2 != 0:
    hex_data = "0" + hex_data  # Prepend zero if needed

ascii_data = binascii.unhexlify(hex_data)
print("ASCII:", ascii_data.decode(errors="replace"))  # Replace errors if any non-printable chars


def caesar_decrypt(ciphertext, shift=3):
    decrypted = []
    for char in ciphertext:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            decrypted.append(chr((ord(char) - base - shift) % 26 + base))
        else:
            decrypted.append(char)
    return ''.join(decrypted)

hex_data = "06c7026a72726720706464762c206c70206f6478756c710a"
ascii_data = binascii.unhexlify(hex_data).decode(errors="ignore")

decrypted_message = caesar_decrypt(ascii_data)
print("Decrypted message:", decrypted_message)

