
import telebot
import openai
from telebot import types

openai.api_key = "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"
bot = telebot.TeleBot("5985109471:AAGL845ixtZ_Ceotf7chGEq8eVEZ4WoVMe4")

watching_list = []

# Define the /start command
@bot.message_handler(commands=['start'])
def send_welcome(message):
    bot.reply_to(message, "Welcome to the Recommendations Bot. How can I assist you?")

# Define the /add command
@bot.message_handler(commands=['add'])
def add_watching_list(message):
    msg = bot.reply_to(message, "Please enter the name of the anime you want to add to your watching list.")
    bot.register_next_step_handler(msg, process_add_watching_list)

def process_add_watching_list(message):
    anime = message.text
    watching_list.append(anime)
    bot.reply_to(message, "Anime " + anime + " has been added to your watching list.")

# Define the /remove command
@bot.message_handler(commands=['remove'])
def remove_watching_list(message):
    markup = types.ReplyKeyboardMarkup(one_time_keyboard=True)
    for anime in watching_list:
        markup.add(anime)
    msg = bot.reply_to(message, "Please select the anime you want to remove from your watching list.", reply_markup=markup)
    bot.register_next_step_handler(msg, process_remove_watching_list)

def process_remove_watching_list(message):
    anime = message.text
    watching_list.remove(anime)
    bot.reply_to(message, "Anime " + anime + " has been removed from your watching list.")

# Define the /print command
@bot.message_handler(commands=['print'])
def print_watching_list(message):
    if watching_list:
        bot.reply_to(message, "Your watching list: " + ", ".join(watching_list))
    else:
        bot.reply_to(message, "Your watching list is empty.")

# Define the /refresh command
@bot.message_handler(commands=['refresh'])
def refresh_recommendations(message):
    if watching_list:
        prompt = "Based on my watching list, recommend me a movie, a series, and an anime."
        completions = openai.Completion.create(engine="text-davinci-002", prompt=prompt, max_tokens=50)
        message = "Here are your recommendations:\\n"
        for i, completion in enumerate(completions.choices):
            message += f"{i+1}. {completion.text.capitalize()}"
        bot.reply_to(message, message)
    else:
        bot.reply_to(message, "Your watching list is empty.")

# Start the bot
bot.polling()
