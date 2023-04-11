import telebot
import openai

openai.api_key = "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"
bot = telebot.TeleBot("5985109471:AAGL845ixtZ_Ceotf7chGEq8eVEZ4WoVMe4")

a_watching_list = []
m_watching_list = []
s_watching_list = []
category = ""


@bot.message_handler(commands=['start'])
def start(message):
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('Anime')
    btn2 = telebot.types.KeyboardButton('Movie')
    btn3 = telebot.types.KeyboardButton('Serials')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose a category:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'Anime')
def anime(message):
    global category
    category = "a"
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('AnimeWatchingList')
    btn2 = telebot.types.KeyboardButton('Recommendations')
    btn3 = telebot.types.KeyboardButton('Return back')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose an action:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'Movie')
def movie(message):
    global category
    category = "m"
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('MovieWatchingList')
    btn2 = telebot.types.KeyboardButton('Recommendations')
    btn3 = telebot.types.KeyboardButton('Return back')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose an action:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'Serials')
def serials(message):
    global category
    category = "s"
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('SerialsWatchingList')
    btn2 = telebot.types.KeyboardButton('Recommendations')
    btn3 = telebot.types.KeyboardButton('Return back')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose an action:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'AnimeWatchingList')
def anime_watching_list(message):
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('Add')
    btn2 = telebot.types.KeyboardButton('Print')
    btn3 = telebot.types.KeyboardButton('Return back')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose an action:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'MovieWatchingList')
def movie_watching_list(message):
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('Add')
    btn2 = telebot.types.KeyboardButton('Print')
    btn3 = telebot.types.KeyboardButton('Return back')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose an action:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'SerialsWatchingList')
def serials_watching_list(message):
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('Add')
    btn2 = telebot.types.KeyboardButton('Print')
    btn3 = telebot.types.KeyboardButton('Return back')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose an action:", reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'Add')
def add_to_watching_list(message):
    bot.send_message(message.chat.id, "What would you like to add to the list?")
    bot.register_next_step_handler(message, process_add_to_watching_list)


def process_add_to_watching_list(message):
    global category
    list_name = f"{category}_watching_list"

    item = ' '.join(message.text.split()[1:])
    globals()[list_name].append(item)

    bot.send_message(message.chat.id, f"{item} has been added to the {category} watching list.")


@bot.message_handler(func=lambda message: message.text == 'Print')
def print_watching_list(message):
    global category
    list_name = f"{category}_watching_list"

    if globals()[list_name] == []:
        bot.send_message(message.chat.id, "The watching list is empty.")
    else:
        for item in globals()[list_name]:
            bot.send_message(message.chat.id, item)


@bot.message_handler(func=lambda message: message.text == 'Recommendations')
def generate_recommendations(message):
    global category
    watching_list = f"{category}_watching_list"

    if len(watching_list) > 0:
        # Call OpenAI's GPT-3 to generate recommendations based on the watching list
        prompt = f"Recommend me 3 {category} based on my watching list: {', '.join(watching_list)}"
        response = openai.Completion.create(
            engine="davinci",
            prompt=prompt,
            max_tokens=100,
            n=1,
            stop=None,
            temperature=0.5,
        )
        recommendations = response.choices[0].text.strip().split('\n')
        recommendations = [r.split(':') for r in recommendations]
        recommendations = '\n'.join([f"{r[0]}: {r[1]} episodes, {r[2]}" for r in recommendations])
    else:
        recommendations = "There are no recommendations available since the watching list is empty."
        markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
        btn1 = telebot.types.KeyboardButton('Refresh')
        btn2 = telebot.types.KeyboardButton('Return back')
        markup.add(btn1, btn2)
        bot.send_message(message.chat.id, recommendations, reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'Refresh')
def refresh_recommendations(message):
    bot.send_message(message.chat.id, "Refreshing recommendations...")
    bot.register_next_step_handler(message, process_refresh_recommendations)


def process_refresh_recommendations(message):

    global category
    watching_list = f"{category}_watching_list"

    if len(watching_list) > 0:
        # Call OpenAI's GPT-3 to generate recommendations based on the watching list
        prompt = f"Recommend me 3 {category} based on my watching list: {', '.join(watching_list)}"
        response = openai.Completion.create(
            engine="davinci",
            prompt=prompt,
            max_tokens=100,
            n=1,
            stop=None,
            temperature=0.5,
        )
        recommendations = response.choices[0].text.strip().split('\n')
        recommendations = [r.split(':') for r in recommendations]
        recommendations = '\n'.join([f"{r[0]}: {r[1]} episodes, {r[2]}" for r in recommendations])
    else:
        recommendations = "There are no recommendations available since the watching list is empty."
        markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
        btn1 = telebot.types.KeyboardButton('Refresh')
        btn2 = telebot.types.KeyboardButton('Return back')
        markup.add(btn1, btn2)
        bot.send_message(message.chat.id, recommendations, reply_markup=markup)


@bot.message_handler(func=lambda message: message.text == 'Return back')
def return_to_main_menu(message):
    markup = telebot.types.ReplyKeyboardMarkup(row_width=1)
    btn1 = telebot.types.KeyboardButton('Anime')
    btn2 = telebot.types.KeyboardButton('Movie')
    btn3 = telebot.types.KeyboardButton('Serials')
    markup.add(btn1, btn2, btn3)
    bot.send_message(message.chat.id, "Choose a category:", reply_markup=markup)


bot.polling()
