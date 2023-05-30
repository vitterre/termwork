import matplotlib.pyplot as plt

steps_for_finding = []
steps_for_insertions = []
steps_for_removing = []

times_for_finding = []
times_for_insertions = []
times_for_removing = []


with open('steps_for_finding.txt') as f:
	for line in f:
		steps_for_finding.append(int(line))


with open('steps_for_insertions.txt') as f:
	for line in f:
		steps_for_insertions.append(int(line))


with open('steps_for_removing.txt') as f:
	for line in f:
		steps_for_removing.append(int(line))


with open('times_for_finding.txt') as f:
	for line in f:
		times_for_finding.append(int(line))


with open('times_for_insertions.txt') as f:
	for line in f:
		times_for_insertions.append(int(line))


with open('times_for_removing.txt') as f:
	for line in f:
		times_for_removing.append(int(line))

print(steps_for_finding[0:10])
print(steps_for_insertions[0:10])
print(steps_for_removing[0:10])

print(times_for_finding[0:10])
print(times_for_insertions[0:10])
print(times_for_removing[0:10])

# fig, axs = plt.subplots(3, 1, figsize=(8, 12))

# axs[0].plot(steps_for_finding, label='steps', color='blue')
# axs[0].legend()

# axs01 = axs[0].twinx()
# axs01.plot(times_for_finding, label='time', color='red')
# axs01.legend()

# plt.show()
# Создание 3 окон для графиков

fig, ax = plt.subplots()
ax.plot(list(range(len(steps_for_finding))), steps_for_finding, label='steps')
ax.plot(list(range(len(times_for_finding))), times_for_finding, label='times')
ax.set_title("Remove MIN")
ax.legend()
plt.show()

plt.plot(list(range(len(steps_for_insertions))), steps_for_insertions, label='steps')
plt.plot(list(range(len(times_for_insertions))), times_for_insertions, label='steps')
plt.suptitle("Insert")
plt.legend()
plt.show()

plt.plot(list(range(len(steps_for_removing))), steps_for_removing, label='steps')
plt.plot(list(range(len(times_for_removing))), times_for_removing, label='steps')
plt.suptitle("Delete")
plt.legend()
plt.show()
